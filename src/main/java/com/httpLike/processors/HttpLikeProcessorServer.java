package com.httpLike.processors;

import com.UDP.processors.ResponseProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.models.HttpLikeRequestModel;
import com.httpLike.models.HttpLikeResponseModel;
import com.httpLike.models.Controller;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpLikeProcessorServer implements ResponseProcessor {
    private static ObjectMapper mapper = new ObjectMapper();
    private static String httpLikeVersion = "0.0.0";
    private static List<String> existingMethods = Arrays.asList("POST", "GET", "CHANGE", "DELETE", "ALL");

    public static String getHttpLikeVersion() {
        return httpLikeVersion;
    }

    private ArrayList<Controller> controllers = new ArrayList<>();

    @Override
    public String processResponse(String request) {
        HttpLikeRequestModel requestModel;
        try {
            requestModel = mapper.readValue(request, HttpLikeRequestModel.class);
        } catch (JsonProcessingException e) {
            return new HttpLikeResponseModel(
                    httpLikeVersion,
                    new HashMap<>(),
                    e.getMessage(),
                    "Invalid HttpLike format.",
                    406
            ).toString();
        }

        if (!existingMethods.contains(requestModel.getMethod())) {
            return new HttpLikeResponseModel(
                    httpLikeVersion,
                    new HashMap<>(),
                    "Method " + requestModel.getMethod(),
                    "No such method",
                    405
            ).toString();
        }

        Pattern pattern = Pattern.compile("\\{.*}");

        for(Controller controller : controllers){
            Logger.getLogger("bla").error(requestModel.getRoute().replaceAll("\\{.*}", "\\\\{.*}"));
            if(controller.getMethod().equals(requestModel.getMethod()) &&
               controller.getRoute().matches(requestModel.getRoute().replaceAll("\\{.*}","\\\\{.*}"))){
                try {
                    ArrayList<String> variables;
                    if(controller.getPathVariables() == null){
                        Matcher matcherVariables = pattern.matcher(controller.getRoute());
                        variables = new ArrayList<>();
                        while (matcherVariables.find()){
                            variables.add(matcherVariables.group());
                        }
                    } else {
                        variables = controller.getPathVariables();
                    }

                    Matcher matcherValues = pattern.matcher(requestModel.getRoute());
                    ArrayList<String> values = new ArrayList<>();


                    while (matcherValues.find()){
                        values.add(matcherValues.group());
                    }
                    HashMap<String,String> pathVariables = new HashMap<>();
                    for(int i = 0; i < values.size(); i++){
                        pathVariables.put(
                                variables.get(i).substring(1,variables.get(i).length() - 1),
                                values.get(i).substring(1,values.get(i).length() - 1)
                        );
                    }

                    requestModel.setPathVariables(pathVariables);
                    return controller.getProcessor().processRequest(requestModel).toString();
                } catch (Throwable e) {
                    return new HttpLikeResponseModel(
                            httpLikeVersion,
                            new HashMap<>(),
                            e.getMessage(),
                            "Internal server error",
                            500
                    ).toString();
                }
            }
        }

        return new HttpLikeResponseModel(
                httpLikeVersion,
                new HashMap<>(),
                "Not found method - " + requestModel.getMethod() + ", on route - " + requestModel.getRoute(),
                "Not found",
                404
        ).toString();
    }

    public void addController(Controller controller){
        controllers.add(controller);
    }

    public void addController(String method,String route,HttpLikeRequestProcessor processor){
        controllers.add(new Controller(method,route,processor));
    }

    public void addController(String route,HttpLikeRequestProcessor processor){
        controllers.add(new Controller(route,processor));
    }



}

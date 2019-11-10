package com.httpLike.processors;

import com.UDP.processors.ResponseProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.models.HttpLikeRequestModel;
import com.httpLike.models.HttpLikeResponseModel;
import com.httpLike.models.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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


        for(Controller route : controllers){
            if(route.getMethod().equals(requestModel.getMethod()) &&
               route.getRoute().equals(requestModel.getRoute())){
                try {
                    return route.getProcessor().processRequest(requestModel).toString();
                } catch (Throwable e) {
                    return new HttpLikeResponseModel(
                            httpLikeVersion,
                            new HashMap<>(),
                            "Internal server error.",
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

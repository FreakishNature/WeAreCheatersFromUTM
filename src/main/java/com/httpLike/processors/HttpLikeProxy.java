package com.httpLike.processors;

import com.UDP.processors.ResponseProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.models.Controller;
import com.httpLike.models.HttpLikeRequestModel;
import com.httpLike.models.HttpLikeResponseModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HttpLikeProxy implements ResponseProcessor {
    private static List<String> existingMethods = Arrays.asList("POST", "GET", "CHANGE", "DELETE", "ALL");
    private HttpLikeProcessor httpLikeProcessor = HttpLikeProcessor.getInstance();
    private static HttpLikeProxy proxy;
    private ArrayList<Controller> controllers = new ArrayList<>();

    private static ObjectMapper mapper = new ObjectMapper();

    HttpLikeProxy() {

    }

    public static synchronized HttpLikeProxy getInstance() {
        if (proxy == null) {
            proxy = new HttpLikeProxy();
        }
        return proxy;
    }

    @Override
    public String processResponse(String request) {
        httpLikeProcessor.setControllers(controllers);
        HttpLikeRequestModel requestModel;
        try {
            requestModel = mapper.readValue(request, HttpLikeRequestModel.class);

        } catch (JsonProcessingException e) {
            return new HttpLikeResponseModel.Builder()
                    .withHeaders(new HashMap<>())
                    .withMsg("Invalid HttpLike format")
                    .withBody(e.getMessage())
                    .withStatusCode(406)
                    .build()
                    .toString();
        }

        if (!existingMethods.contains(requestModel.getMethod())) {
            return new HttpLikeResponseModel.Builder()
                    .withHeaders(new HashMap<>())
                    .withMsg("No such method")
                    .withBody("Method " + requestModel.getMethod())
                    .withStatusCode(405)
                    .build()
                    .toString();
        }

        if (requestModel.getPath().charAt(0) != "/".charAt(0)) {
            return new HttpLikeResponseModel.Builder()
                    .withHeaders(new HashMap<>())
                    .withMsg("Bad Request")
                    .withBody("Path " + requestModel.getPath())
                    .withStatusCode(400)
                    .build()
                    .toString();
        }

        return httpLikeProcessor.processResponse(request);
    }

    public void addController(String method, String route, HttpLikeRequestProcessor processor) {
        controllers.add(new Controller(method, route, processor));
    }
}

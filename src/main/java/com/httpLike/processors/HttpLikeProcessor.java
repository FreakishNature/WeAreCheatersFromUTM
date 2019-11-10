package com.httpLike.processors;

import com.UDP.processors.RequestProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.models.HttpLikeRequestModel;
import com.httpLike.models.HttpLikeResponseModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HttpLikeProcessor implements RequestProcessor {
    private static ObjectMapper mapper = new ObjectMapper();
    private static String httpLikeVersion = "0.0.0";
    private static List<String> existingMethods = Arrays.asList("POST", "GET", "CHANGE", "DELETE");
    private HttpLikeRequestProcessor processor;

    @Override
    public String processRequest(String request) {
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


        HttpLikeResponseModel httpLikeResponseModel = processor.processRequest(requestModel);

        return httpLikeResponseModel.toString();
    }


    public HttpLikeProcessor(HttpLikeRequestProcessor processor) {
        this.processor = processor;
    }
}

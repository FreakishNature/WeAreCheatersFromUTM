package com.httpLike.processors;

import com.UDP.processors.RequestProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.responses.StandardResponses;
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
                    StandardResponses.invalidHttpLikeFormat.getMessage(),
                    StandardResponses.invalidHttpLikeFormat.getMessage(),
                    StandardResponses.invalidHttpLikeFormat.getCode()
            ).toString();
        }

        if (!existingMethods.contains(requestModel.getMethod())) {
            return new HttpLikeResponseModel(
                    httpLikeVersion,
                    new HashMap<>(),
                    StandardResponses.noSuchMethod.getMessage(),
                    StandardResponses.noSuchMethod.getMessage(),
                    StandardResponses.noSuchMethod.getCode()
            ).toString();
        }


        HttpLikeResponseModel httpLikeResponseModel = processRequest(requestModel);

        return httpLikeResponseModel.toString();
    }

    HttpLikeResponseModel processRequest(HttpLikeRequestModel request) {
        return new HttpLikeResponseModel();
    }

    public HttpLikeProcessor(HttpLikeRequestProcessor processor) {
        this.processor = processor;
    }
}

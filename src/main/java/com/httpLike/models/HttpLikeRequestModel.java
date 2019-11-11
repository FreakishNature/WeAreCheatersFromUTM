package com.httpLike.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.processors.HttpLikeProcessorServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HttpLikeRequestModel {

    private static ObjectMapper mapper = new ObjectMapper();
    String protocol = HttpLikeProcessorServer.getHttpLikeVersion();
    String route;
    String method;
    HashMap<String, String> headers;
    String body;
    HashMap<String,String> pathVariables;

    public HttpLikeRequestModel(String route, String method, HashMap<String, String> headers, String body) {
        this.route = route;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public HttpLikeRequestModel(String protocol, String route, String method, HashMap<String, String> headers, String body) {
        this.protocol = protocol;
        this.route = route;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public String getPathVariable(String variable){
        return pathVariables.get(variable);
    }
}

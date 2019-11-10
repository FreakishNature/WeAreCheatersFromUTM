package com.httpLike.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.processors.HttpLikeProcessorServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HttpLikeResponseModel {
    private static ObjectMapper mapper = new ObjectMapper();
    private String protocol = HttpLikeProcessorServer.getHttpLikeVersion();
    HashMap<String, String> headers;
    private String body;
    private String message;
    private int code;

    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public HttpLikeResponseModel(HashMap<String, String> headers, String body, String message, int code) {
        this.headers = headers;
        this.body = body;
        this.message = message;
        this.code = code;
    }
}

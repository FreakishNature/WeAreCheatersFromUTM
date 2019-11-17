package com.httpLike.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.processors.HttpLikeProcessor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class HttpLikeResponseModel {
    private static ObjectMapper mapper = new ObjectMapper();
    private String protocol = HttpLikeProcessor.getHttpLikeVersion();
    private HashMap<String, String> headers;
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

    private HttpLikeResponseModel() {
    }

    public static class Builder {
        private HashMap<String, String> headers;
        private String body;
        private String message;
        private int code;

        public Builder() {
        }

        public Builder withHeaders(HashMap<String, String> headers){
            this.headers = headers;
            return this;
        }
        public Builder withBody(String body){
            this.body = body;
            return this;
        }
        public Builder withMsg(String message){
            this.message = message;
            return this;
        }
        public Builder withStatusCode(int code){
            this.code = code;
            return this;
        }
        public HttpLikeResponseModel build(){
            HttpLikeResponseModel response = new HttpLikeResponseModel();
            response.headers = this.headers;
            response.body = this.body;
            response.message = this.message;
            response.code = this.code;
            return response;
        }
    }
}

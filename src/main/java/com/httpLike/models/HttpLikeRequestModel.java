package com.httpLike.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.processors.HttpLikeProcessor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;


@Getter
@Setter
public class HttpLikeRequestModel {

    private static ObjectMapper mapper = new ObjectMapper();
    String protocol = HttpLikeProcessor.getHttpLikeVersion();
    String path;
    String method;
    HashMap<String, String> headers;
    String body;
    HashMap<String, String> pathVariables;

    private HttpLikeRequestModel() {
    }

    public static class Builder {
        private HashMap<String, String> headers;
        private String body;
        private String method;
        private String path;

        public Builder() {
        }

        public Builder withHeaders(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withBody(String body) {
            this.body = body;
            return this;
        }

        public Builder usingMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public HttpLikeRequestModel build() {
            HttpLikeRequestModel request = new HttpLikeRequestModel();
            request.headers = this.headers;
            request.body = this.body;
            request.method = this.method;
            request.path = this.path;
            return request;
        }
    }

    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public String getPathVariable(String variable) {
        return pathVariables.get(variable);
    }
}

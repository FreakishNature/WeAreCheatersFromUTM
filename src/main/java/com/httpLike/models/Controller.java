package com.httpLike.models;

import com.httpLike.processors.HttpLikeRequestProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Controller {
    String method = "ALL";
    String route;
    HttpLikeRequestProcessor processor;
    ArrayList<String> pathVariables;

    public Controller(String method, String route, HttpLikeRequestProcessor processor) {
        this.method = method;
        this.route = route;
        this.processor = processor;
    }

    public Controller(String route, HttpLikeRequestProcessor processor) {
        this.route = route;
        this.processor = processor;
    }
}

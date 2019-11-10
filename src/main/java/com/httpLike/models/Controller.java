package com.httpLike.models;

import com.httpLike.processors.HttpLikeRequestProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Controller {
    String method = "ALL";
    String route;
    HttpLikeRequestProcessor processor;

    public Controller(String route, HttpLikeRequestProcessor processor) {
        this.route = route;
        this.processor = processor;
    }
}

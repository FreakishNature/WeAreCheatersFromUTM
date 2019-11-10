package com.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HttpLikeRequestModel {
    String protocol;
    String route;
    String method;
    HashMap<String,String> headers;
    String body;
}

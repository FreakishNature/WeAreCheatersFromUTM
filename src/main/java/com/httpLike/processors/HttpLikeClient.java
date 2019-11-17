package com.httpLike.processors;

import com.UDP.ClientDecorator;
import com.UDP.UdpClient;
import com.httpLike.models.HttpLikeRequestModel;

import java.util.HashMap;

public class HttpLikeClient extends ClientDecorator {

    public String send(String method, String path, String body) {
        HttpLikeRequestModel requestModel = new HttpLikeRequestModel.Builder()
                .usingMethod(method)
                .withHeaders(new HashMap<>())
                .withBody(body)
                .withPath(path)
                .build();

        return super.send(requestModel.toString(), 1);
    }

    public HttpLikeClient(UdpClient udpClient) {
        super(udpClient);
    }
}

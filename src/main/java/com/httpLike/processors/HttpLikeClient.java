package com.httpLike.processors;

import com.UDP.Client;
import com.UDP.processors.RequestProcessor;
import com.httpLike.models.HttpLikeRequestModel;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class HttpLikeClient{
    Client client;

    public String send(String method,String route,String body) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
        HttpLikeRequestModel requestModel = new HttpLikeRequestModel(
                route,
                method,
                new HashMap<>(),
                body
        );

        return client.send(requestModel.toString(),1);
    }

    public HttpLikeClient(String host,int port) throws SocketException, UnknownHostException {
        client = new Client(host,port);
    }
}

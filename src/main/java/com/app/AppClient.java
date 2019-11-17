package com.app;

import com.UDP.UdpClient;
import com.app.model.DataRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.processors.HttpLikeClient;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class AppClient {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();

        HttpLikeClient client = new HttpLikeClient(new UdpClient("localhost", 4000));
        System.out.println(client.send("POST", "/data", mapper.writeValueAsString(new DataRequest("1", "wooow"))));
        System.out.println(client.send("GET", "/data", ""));
        System.out.println(client.send("GET", "/data/1", ""));
        System.out.println(client.send("CHANGE", "/data/1", mapper.writeValueAsString(new DataRequest("1", "BLOO"))));
        System.out.println(client.send("GET", "/data/1", ""));
        System.out.println(client.send("DELETE", "/data/1", mapper.writeValueAsString(new DataRequest("1", "BLOO"))));
        System.out.println(client.send("GET", "/data/1", ""));
        System.out.println(client.send("GET", "/notfound", ""));

    }
}

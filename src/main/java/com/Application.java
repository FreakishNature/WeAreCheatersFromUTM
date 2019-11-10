package com;

import com.UDP.Server;
import com.httpLike.processors.HttpLikeProcessor;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {

        new Server(4000, new HttpLikeProcessor(
                request -> {
                    if (request.getMethod().equals("GET") && request.getRoute().equals("/home")) {

                        return null;
                    }
                    return null;
                }
        )).start();
    }
}

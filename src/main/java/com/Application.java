package com;

import com.UDP.Server;
import com.httpLike.HttpLikeProcessor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {

        new Server(4000,new HttpLikeProcessor(
                request -> {
                    if(request.getMethod().equals("GET") && request.getRoute().equals("/home")){

                        return null;
                    }
                    return null;
                }
        ));
    }
}

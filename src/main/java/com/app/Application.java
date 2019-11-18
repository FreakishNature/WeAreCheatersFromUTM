package com.app;

import com.UDP.UdpServer;
import com.app.model.DataRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.models.HttpLikeResponseModel;
import com.httpLike.processors.HttpLikeProxy;

import java.io.IOException;
import java.util.HashMap;

public class Application {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> data = new HashMap<>();

        HttpLikeProxy httpLikeProxy = HttpLikeProxy.getInstance();

        httpLikeProxy.addController("POST", "/data", request -> {
            DataRequest dataRequest = mapper.readValue(request.getBody(), DataRequest.class);
            data.put(dataRequest.getKey(), dataRequest.getData());

            return new HttpLikeResponseModel.Builder()
                    .withHeaders(new HashMap<>())
                    .withMsg("Created")
                    .withBody("")
                    .withStatusCode(201)
                    .build();
        });

        httpLikeProxy.addController("GET", "/data", request -> new HttpLikeResponseModel.Builder()
                .withHeaders(new HashMap<>())
                .withMsg("OK")
                .withBody("")
                .withStatusCode(200)
                .build()
        );

        httpLikeProxy.addController("GET", "/data/{key}", request -> new HttpLikeResponseModel.Builder()
                .withHeaders(new HashMap<>())
                .withMsg("OK")
                .withBody(mapper.writeValueAsString(data.get(request.getPathVariable("key"))))
                .withStatusCode(200)
                .build()
        );

        httpLikeProxy.addController("CHANGE", "/data/{key}", request ->
                {
                    DataRequest dataRequest = mapper.readValue(request.getBody(), DataRequest.class);
                    data.put(request.getPathVariable("key"), dataRequest.getData());

                    return new HttpLikeResponseModel.Builder()
                            .withHeaders(new HashMap<>())
                            .withMsg("Accepted")
                            .withBody("")
                            .withStatusCode(204)
                            .build();
                }
        );

        httpLikeProxy.addController("DELETE", "/data/{key}", request ->
                {
                    data.remove(request.getPathVariable("key"));
                    return new HttpLikeResponseModel.Builder()
                            .withHeaders(new HashMap<>())
                            .withMsg("Accepted")
                            .withBody("")
                            .withStatusCode(204)
                            .build();
                }
        );
        UdpServer.getInstance(4000, httpLikeProxy).run();
    }
}

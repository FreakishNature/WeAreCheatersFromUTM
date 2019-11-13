package com.app;

import com.app.model.DataRequest;
import com.UDP.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httpLike.models.HttpLikeResponseModel;
import com.httpLike.processors.HttpLikeProcessorServer;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Application {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String,String> data = new HashMap<>();

        HttpLikeProcessorServer requestProcessor = new HttpLikeProcessorServer();

        requestProcessor.addController("POST","/data",request ->{
            DataRequest dataRequest = mapper.readValue(request.getBody(), DataRequest.class);
            data.put(dataRequest.getKey(),dataRequest.getData());

            return new HttpLikeResponseModel(
                    new HashMap<>(),
                    "",
                    "Created",
                    201
            );
        });

        requestProcessor.addController("GET","/data",request -> new HttpLikeResponseModel(
                new HashMap<>(),
                mapper.writeValueAsString(data),
                "Ok.",
                200
        ));

        requestProcessor.addController("GET","/data/{key}",request -> new HttpLikeResponseModel(
                        new HashMap<>(),
                        mapper.writeValueAsString(data.get(request.getPathVariable("key"))),
                        "Ok.",
                        200
                )
        );

        requestProcessor.addController("CHANGE","/data/{key}",request ->
            {
                DataRequest dataRequest = mapper.readValue(request.getBody(), DataRequest.class);
                data.put(request.getPathVariable("key"),dataRequest.getData());

                return new HttpLikeResponseModel(
                        new HashMap<>(),
                        "",
                        "Accepted.",
                        204
                );
            }
        );

        requestProcessor.addController("DELETE","/data/{key}",request ->
                {
                    data.remove(request.getPathVariable("key"));
                    return new HttpLikeResponseModel(
                            new HashMap<>(),
                            "",
                            "Accepted.",
                            204
                    );
                }
        );
        new Server(4000, requestProcessor).run();
    }
}

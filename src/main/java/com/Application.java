package com;

import com.UDP.Client;
import com.UDP.Server;
import com.model.Request;
import com.security.PacketProcessor;
import com.utils.ByteConverter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        byte[] buf = PacketProcessor.preparePacket("test", 0, Server.keys);

        Request request = PacketProcessor.unpackPacket(ByteConverter.toString(buf), Client.keys);

        System.out.println(request.getMessage());
        System.out.println(request.getAttempt());
        System.out.println(request.getHashSum());

        System.out.println("-----------------------------");

        buf = PacketProcessor.preparePacket("1234", 0, Client.keys);

        request = PacketProcessor.unpackPacket(ByteConverter.toString(buf), Server.keys);

        System.out.println(request.getMessage());
        System.out.println(request.getAttempt());
        System.out.println(request.getHashSum());


    }
}

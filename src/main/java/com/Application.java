package com;

import com.UDP.Client;
import com.UDP.Server;
import com.security.ServerKeys;
import com.utils.ByteConverter;
import com.security.CryptoConverter;
import com.security.PacketProcessor;
import com.model.Request;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        byte[] buf = PacketProcessor.preparePacket("test",0, Server.keys);

        Request request = PacketProcessor.unpackPacket(ByteConverter.toString(buf), Client.keys);

        System.out.println(request.getMessage());
        System.out.println(request.getAttempt());
        System.out.println(request.getHashSum());


    }
}

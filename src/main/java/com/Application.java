package com;

import com.UDP.EchoServer;
import com.security.ByteConverter;
import com.security.CryptoConverterShaRsa;
import com.security.PacketProcessor;
import com.security.Request;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        byte[] buf = PacketProcessor.preparePacket("test",0);


        String packetDecrypted = CryptoConverterShaRsa
                .decrypt(ByteConverter.toString(buf));

        assert packetDecrypted != null;
        Request request = PacketProcessor.requestFromPacket(packetDecrypted);
        request.setHashSum("123");
        boolean isValidPacket = PacketProcessor.validatePacket(request);

        if(!isValidPacket){
            System.out.println("Is not valid packet");
            String errorMessage = "Message was send badly";
            byte[] response = PacketProcessor.preparePacket(errorMessage, request.getAttempt() + 1);
            System.out.println(CryptoConverterShaRsa.decrypt(ByteConverter.toString(response)));
        }

    }
}

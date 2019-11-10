package com;

import com.security.ByteConverter;
import com.security.CryptoConverter;
import com.security.PacketProcessor;
import com.security.Request;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Application {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        byte[] buf = PacketProcessor.preparePacket("test",0);


        String packetDecrypted = CryptoConverter
                .decrypt(ByteConverter.toString(buf));

        assert packetDecrypted != null;
        Request request = PacketProcessor.requestFromPacket(packetDecrypted);
        request.setHashSum("123");
        boolean isValidPacket = PacketProcessor.validatePacket(request);

        if(!isValidPacket){
            System.out.println("Is not valid packet");
            String errorMessage = "Message was send badly";
            byte[] response = PacketProcessor.preparePacket(errorMessage, request.getAttempt() + 1);
            System.out.println(CryptoConverter.decrypt(ByteConverter.toString(response)));
        }

    }
}

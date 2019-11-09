package com.security;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class PacketProcessor {
    private static String separator = "<|>";
    private static String separatorRegex = "<\\|>";

    public static byte[] preparePacket(String msg,int attempt) throws NoSuchAlgorithmException {
        String hashSum = CryptoConverterShaRsa.getHashSum(msg);
        String packetNonEncrypted = hashSum  + separator + msg + separator + attempt;
        return ByteConverter
                .toByteArray(
                        Objects.requireNonNull(CryptoConverterShaRsa
                                .encrypt(packetNonEncrypted)
                        )
                );
    }

    public static boolean validatePacket(Request request) throws NoSuchAlgorithmException {
        return CryptoConverterShaRsa.getHashSum(request.getMessage()).equals(request.getHashSum());
    }

    public static Request requestFromPacket(String packet){
        String[] packetSplitted = packet.split(separatorRegex);
        return new Request(packetSplitted[0], packetSplitted[1], Integer.parseInt(packetSplitted[2]));
    }
}

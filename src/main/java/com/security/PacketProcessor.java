package com.security;

import com.model.EncryptedData;
import com.model.Request;
import com.utils.ByteConverter;
import com.utils.Serialization;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class PacketProcessor {
    private static String separator = "<|>";
    private static String separatorRegex = "<\\|>";

    public static byte[] preparePacket(String msg, int attempt, Keys keys) throws NoSuchAlgorithmException, IOException {
        String hashSum = CryptoConverter.getHashSum(msg);
        EncryptedData encryptedData = CryptoConverter.encrypt(msg, keys.aesKey, keys.rsaPublicKey, keys.dsaPrivateKey);

        String serverPacket = hashSum + separator +
                Serialization.toString(encryptedData) + separator +
                attempt;

        return ByteConverter.toByteArray(serverPacket);
    }


    public static boolean validatePacket(Request request) throws NoSuchAlgorithmException {
        return CryptoConverter.getHashSum(request.getMessage()).equals(request.getHashSum());
    }

    public static Request requestFromPacket(String packet) {
        String[] packetSplitted = packet.split(separatorRegex);
        return new Request(packetSplitted[0], packetSplitted[1], Integer.parseInt(packetSplitted[2]));
    }

    public static void validateAndSend(DatagramSocket socket, Request request, DatagramPacket packet, String message) throws IOException, NoSuchAlgorithmException {
        validateAndSend(socket, request, packet, message, 0);
    }

    public static void validateAndSend(DatagramSocket socket, Request request, DatagramPacket packet, String message, int errorAttempt) throws NoSuchAlgorithmException, IOException {
//        boolean isValidCheckSum = PacketProcessor.validatePacket(request);
//
//        byte[] buf = !isValidCheckSum
//                ? PacketProcessor.preparePacket(Server.errorMessage,request.getAttempt() + 1)
//                : PacketProcessor.preparePacket(
//                message,errorAttempt);
//
//        packet.setData(buf);
//
//        socket.send(packet);

    }

    public static Request receiveAndDecrypt(DatagramSocket socket, DatagramPacket packet, byte[] buf, Keys keys) throws IOException, ClassNotFoundException {
        socket.receive(packet);

        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);

        String received
                = new String(packet.getData(), 0, packet.getLength());

        return unpackPacket(received, keys);
    }

    public static Request unpackPacket(String received, Keys keys) throws IOException, ClassNotFoundException {
        String[] receivedSplit = received.split(separatorRegex);

        String hashSum = receivedSplit[0];
        EncryptedData encryptedData = (EncryptedData) Serialization.fromString(receivedSplit[1]);
        String message = CryptoConverter.decrypt(encryptedData, keys.rsaPrivateKey, keys.dsaPublicKey);


        int attempt = Integer.parseInt(receivedSplit[2]);

        return new Request(hashSum, message, attempt);
    }

}

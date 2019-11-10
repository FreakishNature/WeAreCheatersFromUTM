package com.security;

import com.UDP.Server;
import com.UDP.exceptions.ServerException;
import com.model.EncryptedData;
import com.model.Request;
import com.utils.ByteConverter;
import com.utils.Serialization;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;

public class PacketProcessor {
    private static Logger logger = Logger.getLogger(PacketProcessor.class.getName());
    private static String separator = "<|>";
    private static String separatorRegex = "<\\|>";

    public static byte[] preparePacket(String msg, int attempt, Keys keys) throws NoSuchAlgorithmException, IOException {
        logger.info("Starting preparing packet.");
        String hashSum = CryptoConverter.getHashSum(msg);
        logger.info("Calculated hash sum.");
        EncryptedData encryptedData = CryptoConverter.encrypt(msg, keys.aesKey, keys.rsaPublicKey, keys.dsaPrivateKey);
        logger.info("Encrypted data.");

        String serverPacket = hashSum + separator +
                Serialization.toString(encryptedData) + separator +
                attempt;

        logger.debug("Packet : " + serverPacket);
        logger.info("Finished packet preparation.");
        return ByteConverter.toByteArray(serverPacket);
    }


    public static boolean validatePacket(Request request) throws NoSuchAlgorithmException {
        logger.info("Check if hash sum is valid");
        boolean isValidPacket = CryptoConverter.getHashSum(request.getMessage()).equals(request.getHashSum());
        if(!isValidPacket){
            logger.warn("Hash sum is not valid");
        }
        return isValidPacket;
    }

    public static Request requestFromPacket(String packet) {
        String[] packetSplit = packet.split(separatorRegex);
        return new Request(packetSplit[0], packetSplit[1], Integer.parseInt(packetSplit[2]));
    }

    public static void validateAndSend(DatagramSocket socket, Request request, DatagramPacket packet, String message,Keys keys) throws IOException, NoSuchAlgorithmException {
        validateAndSend(socket,request,packet,message,0,keys);
    }
    public static void validateAndSend(DatagramSocket socket, Request request, DatagramPacket packet, String message, int errorAttempt,Keys keys) throws NoSuchAlgorithmException, IOException {
        boolean isValidCheckSum = PacketProcessor.validatePacket(request);

        byte[] buf = !isValidCheckSum
                ? PacketProcessor.preparePacket(Server.errorMessage,request.getAttempt() + 1,keys)
                : PacketProcessor.preparePacket(
                message,errorAttempt,keys);

        packet.setData(buf);

        socket.send(packet);

    }

    public static Request receiveAndDecrypt(DatagramSocket socket, DatagramPacket packet, byte[] buf, Keys keys) throws IOException, ClassNotFoundException {
        socket.receive(packet);

        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);

        logger.info("Received message from " + address + ":" + port);

        String received
                = new String(packet.getData(), 0, packet.getLength());
        logger.debug("Received data : " + received);
        logger.info("Unpacking packet");

        return unpackPacket(received, keys);
    }

    public static Request unpackPacket(String received, Keys keys) throws IOException, ClassNotFoundException {
        String[] receivedSplit = received.trim().split(separatorRegex);
        Request request;

        try{
            request  = new Request(
                    receivedSplit[0], // hash sum
                    CryptoConverter.decrypt(
                            (EncryptedData) Serialization.fromString(receivedSplit[1]),
                            keys.rsaPrivateKey,
                            keys.dsaPublicKey
                    ), // decrypted data
                    Integer.parseInt(receivedSplit[2]) // attempt
            );
        }catch (IndexOutOfBoundsException e){
            throw ServerException.receivingResponseException();
        }

        logger.debug("\n\tRequest hash sum : " + request.getHashSum() +
                "\n\tRequest decrypted message : " + request.getMessage() +
                "\n\tRequest attempt : " + request.getAttempt());

        return request;
    }

}

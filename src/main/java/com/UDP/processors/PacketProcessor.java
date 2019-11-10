package com.UDP.processors;

import com.UDP.Server;
import com.UDP.exceptions.ServerException;
import com.security.models.EncryptedData;
import com.UDP.models.Request;
import com.security.SecurityManager;
import com.security.keys.KeyStorage;
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

    public static byte[] preparePacket(String msg, int attempt, KeyStorage keyStorage) throws NoSuchAlgorithmException, IOException {
        logger.info("Starting preparing packet.");
        String hashSum = SecurityManager.getHashSum(msg);
        logger.info("Packet hash sum calculated.");
        EncryptedData encryptedData = SecurityManager.encrypt(msg, keyStorage.getAesKey(), keyStorage.getRsaPublicKey(), keyStorage.getDsaPrivateKey());
        logger.info("Date encrypted.");

        String serverPacket = hashSum + separator +
                Serialization.toString(encryptedData) + separator +
                attempt;

        logger.debug("Packet: " + serverPacket);
        logger.info("Finished packet preparation.");
        return ByteConverter.toByteArray(serverPacket);
    }


    public static boolean validatePacket(Request request) throws NoSuchAlgorithmException {
        logger.info("Checking if hash sum is valid");
        boolean isValidPacket = SecurityManager.getHashSum(request.getMessage()).equals(request.getHashSum());
        if(!isValidPacket){
            logger.warn("Hash sum is not valid");
        }
        return isValidPacket;
    }

    public static Request requestFromPacket(String packet) {
        String[] packetSplit = packet.split(separatorRegex);
        return new Request(packetSplit[0], packetSplit[1], Integer.parseInt(packetSplit[2]));
    }

    public static void validateAndSend(DatagramSocket socket, Request request, DatagramPacket packet, String message, KeyStorage keyStorage) throws NoSuchAlgorithmException, IOException {
        boolean isValidCheckSum = PacketProcessor.validatePacket(request);

        byte[] buf = !isValidCheckSum
                ? PacketProcessor.preparePacket(Server.errorMessage,0, keyStorage)
                : PacketProcessor.preparePacket(message,0, keyStorage);

        packet.setData(buf);

        socket.send(packet);

    }

    public static Request receiveAndDecrypt(DatagramSocket socket, DatagramPacket packet, byte[] buf, KeyStorage keyStorage) throws IOException, ClassNotFoundException {
        socket.receive(packet);

        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);

        logger.info("Received message from " + address + ":" + port);

        String received
                = new String(packet.getData(), 0, packet.getLength());
        logger.debug("Received data : " + received);
        logger.info("Unpacking packet");

        return unpackPacket(received, keyStorage);
    }

    public static Request unpackPacket(String received, KeyStorage keyStorage) throws IOException, ClassNotFoundException {
        String[] receivedSplit = received.trim().split(separatorRegex);
        Request request;

        try{
            request  = new Request(
                    receivedSplit[0], // hash sum
                    SecurityManager.decrypt(
                            (EncryptedData) Serialization.fromString(receivedSplit[1]),
                            keyStorage.getRsaPrivateKey(),
                            keyStorage.getDsaPublicKey()
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

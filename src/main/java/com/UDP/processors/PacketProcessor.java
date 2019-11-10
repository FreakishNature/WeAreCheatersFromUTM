package com.UDP.processors;

import com.UDP.Server;
import com.UDP.exceptions.ServerException;
import com.google.common.base.Splitter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        sendPacketByChunks(packet,socket,buf);
    }
    public static String receivePacketByChunks(DatagramPacket packet, DatagramSocket socket,byte[] buf) throws IOException {
        ArrayList<String> chunks = new ArrayList<>();

        boolean isReceiving = true;
        do{
            socket.receive(packet);
            if(packet.getLength() != 0){
                chunks.add(ByteConverter.toString(buf));
            } else { isReceiving = false; }

                buf = new byte[2048];

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            logger.info("Received message from " + address + ":" + port);

        }while(isReceiving);

        ArrayList<String> chunksSorted = new ArrayList<>(chunks.size());
        chunks.forEach(c -> chunksSorted.add(""));

        chunks.forEach(chunk -> {
            String[] splitChunk = chunk.split("<>");

            int index = Integer.parseInt(splitChunk[0]);
            String chunkData = splitChunk[1];
            chunksSorted.set(index,chunkData);
        });

        return String.join("", chunksSorted);
    }
    public static void sendPacketByChunks(DatagramPacket packet, DatagramSocket socket,byte[] buf) throws IOException {
        ArrayList<String> splitText = new ArrayList<>();
        Splitter.fixedLength(2048 - 8).split(ByteConverter.toString(buf)).forEach(splitText::add);

        for(int i = 0 ; i < splitText.size() ; i++){
            String index = (i+"");
            splitText.set(i,new String(new char[6 - index.length()]).replace('\0','0') + index + "<>" + splitText.get(i));
        }


        for(int i = 0 ; i < (buf.length % 2048) + 1; i++){
            byte[] tmp = ByteConverter.toByteArray(splitText.get(i));
            logger.debug("Sending index - " + i);

            packet.setData(tmp);
            socket.send(packet);
        }

//        packet.setLength(0);
        packet.setData(new byte[0]);
        socket.send(packet);
    }

    public static Request receiveAndDecrypt(DatagramSocket socket, DatagramPacket packet, byte[] buf, KeyStorage keyStorage) throws IOException, ClassNotFoundException {
//        socket.receive(packet);
//
//        InetAddress address = packet.getAddress();
//        int port = packet.getPort();
//        packet = new DatagramPacket(buf, buf.length, address, port);

        String received = receivePacketByChunks(packet,socket,buf);

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
        }catch (IndexOutOfBoundsException | NumberFormatException e){
            throw ServerException.receivingResponseException();
        }

        logger.debug("\n\tRequest hash sum : " + request.getHashSum() +
                "\n\tRequest decrypted message : " + request.getMessage() +
                "\n\tRequest attempt : " + request.getAttempt());

        return request;
    }

}

package com.security;

import com.UDP.EchoServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    public static boolean validateAndSend(DatagramSocket socket, Request request, DatagramPacket packet, String message) throws IOException, NoSuchAlgorithmException {
        return validateAndSend(socket,request,packet,message,0);
    }
    public static boolean validateAndSend(DatagramSocket socket, Request request, DatagramPacket packet, String message, int errorAttempt) throws NoSuchAlgorithmException, IOException {
        boolean isValidCheckSum = PacketProcessor.validatePacket(request);

        byte[] buf = !isValidCheckSum
                ? PacketProcessor.preparePacket(EchoServer.errorMessage,request.getAttempt() + 1)
                : PacketProcessor.preparePacket(
                message,errorAttempt);

        packet.setData(buf);

        socket.send(packet);

        return isValidCheckSum;
    }

    public static String receiveAndDecrypt(DatagramSocket socket,DatagramPacket packet,byte[] buf) throws IOException {
        socket.receive(packet);

        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);

        String received
                = new String(packet.getData(), 0, packet.getLength());

        return CryptoConverterShaRsa.decrypt(received);
    }


}

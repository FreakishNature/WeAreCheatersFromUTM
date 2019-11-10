package com.UDP;

import com.security.ByteConverter;
import com.security.CryptoConverterShaRsa;
import com.security.PacketProcessor;
import com.security.PacketProcessor;
import com.security.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class EchoServer {

    public static String errorMessage = "Message was send badly";
    public static String okMessage = "Ok";
    private DatagramSocket socket;

    public EchoServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }




    public void run() throws IOException, NoSuchAlgorithmException {
        try {
            while (true) {
                byte[] buf = new byte[1024];

                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);

                String packetDecrypted = PacketProcessor.receiveAndDecrypt(socket,packet,buf);

                Request request = PacketProcessor.requestFromPacket(packetDecrypted);
                String responseMsg = processRequestMessage(request.getMessage());
                synchronized (this){
                    PacketProcessor.validateAndSend(socket, request, packet,
                            responseMsg
                    );
                }

                // resending if error
                do{
                    synchronized (this){
                        packetDecrypted = PacketProcessor.receiveAndDecrypt(socket,packet,buf);
                    }

                    request = PacketProcessor.requestFromPacket(packetDecrypted);

                    if(!request.getMessage().equals(okMessage)){
                        synchronized (this){
                            PacketProcessor.validateAndSend(socket, request, packet, responseMsg);
                        }
                    } else{
                        buf = PacketProcessor.preparePacket(okMessage,0);
                        packet.setData(buf);
                        socket.send(packet);
                    }
                }while (request.getAttempt() < 5);
            }
        }finally {
            socket.close();
        }
    }

    private String processRequestMessage(String message) {
        return "good response";
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        new EchoServer(4000).run();
    }
}

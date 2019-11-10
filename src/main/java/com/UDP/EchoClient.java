package com.UDP;

import com.security.ByteConverter;
import com.security.CryptoConverterShaRsa;
import com.security.PacketProcessor;
import com.security.Request;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class EchoClient {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        EchoClient client = new EchoClient("localhost",4000);
        System.out.println(client.sendEcho("hello",0));
    }
    private DatagramSocket socket;
    private InetAddress address;
    int port;
    private byte[] buf;


    public EchoClient(String host,int port) throws SocketException, UnknownHostException {
        this.port = port;
        socket = new DatagramSocket();
        address = InetAddress.getByName(host);
    }

    public String sendEcho(String msg,int attempt) throws IOException, NoSuchAlgorithmException {
        // making packet secured
        // sending first time
        buf = PacketProcessor.preparePacket(msg,attempt);

        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);

        socket.send(packet);

        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        // receiving response

        String packetDecrypted = PacketProcessor.receiveAndDecrypt(socket,packet,buf);
        Request request = PacketProcessor.requestFromPacket(packetDecrypted);
        String serverMsg = request.getMessage();

        if(request.getMessage().equals(EchoServer.errorMessage)){
            if(attempt <= 5){
                serverMsg = sendEcho(msg,attempt + 1);
            }
            return "Server error";
        }

        PacketProcessor.validateAndSend(socket,request,packet,EchoServer.okMessage);

        synchronized (this){
            packetDecrypted = PacketProcessor.receiveAndDecrypt(socket,packet,buf);
        }

        request = PacketProcessor.requestFromPacket(packetDecrypted);

        if(!request.getMessage().equals(EchoServer.okMessage)){
            return "Server error";
        }


        return serverMsg;
    }

    public void close() {
        socket.close();
    }
}

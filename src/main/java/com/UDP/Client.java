package com.UDP;

import com.security.PacketProcessor;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;

public class Client {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Client client = new Client("localhost",4000);
        System.out.println(client.sendEcho("hello"));
    }
    private DatagramSocket socket;
    private InetAddress address;
    int port;
    private byte[] buf;


    public Client(String host, int port) throws SocketException, UnknownHostException {
        this.port = port;
        socket = new DatagramSocket();
        address = InetAddress.getByName(host);
    }

    public String sendEcho(String msg) throws IOException, NoSuchAlgorithmException {
        // making packet secured
        buf = PacketProcessor.preparePacket(msg,0);

        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}

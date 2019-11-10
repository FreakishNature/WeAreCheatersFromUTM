package com.UDP;

import com.security.ByteConverter;
import com.security.CryptoConverter;
import com.security.PacketProcessor;
import com.security.Request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class Server {

    private DatagramSocket socket;
    private byte[] buf = new byte[1024];

    public Server(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }
    private byte[] preparePacket(String msg){
        return new byte[10];

    }

    public void run() throws IOException, NoSuchAlgorithmException {
        while (true) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);

            // processing client request
            String received
                    = new String(packet.getData(), 0, packet.getLength());

            String packetDecrypted = CryptoConverter
                    .decrypt(ByteConverter.toString(buf));

            assert packetDecrypted != null;
            Request request = PacketProcessor.requestFromPacket(packetDecrypted);
            boolean isValidPacket = PacketProcessor.validatePacket(request);

            if(!isValidPacket){
                String errorMessage = "Message was send badly";
                PacketProcessor.preparePacket(errorMessage,request.getAttempt() + 1);
            }

            if (received.equals("end")) {
                break;
            }
            socket.send(packet);
        }
        socket.close();
    }
}

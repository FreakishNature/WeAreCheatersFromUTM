package com.UDP;

import com.model.Request;
import com.security.ClientKeys;
import com.security.Keys;
import com.security.PacketProcessor;
import com.security.ServerKeys;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class Server {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private RequestProcessor requestProcessor;

    static public Keys keys = new Keys(
            ServerKeys.rsaPrivateKey,
            ServerKeys.dsaPrivateKey,
            ClientKeys.rsaPublicKey,
            ClientKeys.dsaPublicKey,
            ServerKeys.aesKey
    );

    public static String errorMessage = "Message was send badly";
    public static String okMessage = "Ok";
    private DatagramSocket socket;

    public Server(int port,RequestProcessor requestProcessor) throws SocketException {
        socket = new DatagramSocket(port);
        this.requestProcessor = requestProcessor;
    }

    public void run() throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        logger.info("Server started");
        try{
            while (true) {
                byte[] buf = new byte[1024];

                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);

                Request request = PacketProcessor.receiveAndDecrypt(socket,packet,buf,keys);

                PacketProcessor.validateAndSend(
                        socket,
                        request,
                        packet,
                        requestProcessor.processRequest(request.getMessage()),
                        keys
                );

            }
        }finally {
            socket.close();
        }

    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
//        new Server(4000).run();
    }
}

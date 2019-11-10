package com.UDP;

import com.UDP.processors.PacketProcessor;
import com.UDP.processors.RequestProcessor;
import com.UDP.models.Request;
import com.security.keys.ClientKeys;
import com.security.keys.KeyStorage;
import com.security.keys.ServerKeys;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class Server {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private RequestProcessor requestProcessor;

    static public KeyStorage keyStorage = new KeyStorage(
            ServerKeys.rsaPrivateKey,
            ServerKeys.dsaPrivateKey,
            ClientKeys.rsaPublicKey,
            ClientKeys.dsaPublicKey,
            ServerKeys.aesKey
    );

    public static String errorMessage = "Message was send badly";
    private DatagramSocket socket;

    public Server(int port, RequestProcessor requestProcessor) throws SocketException {
        socket = new DatagramSocket(port);
        this.requestProcessor = requestProcessor;
    }

    public void run() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
        logger.info("Server started");
        try {
            while (true) {
                byte[] buf = new byte[1024];

                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);

                new Thread(()->{
                    Request request;
                    try {
                        request = PacketProcessor.receiveAndDecrypt(socket, packet, buf, keyStorage);

                        PacketProcessor.validateAndSend(
                                socket,
                                request,
                                packet,
                                requestProcessor.processRequest(request.getMessage()),
                                keyStorage
                        );
                    } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });

            }
        } finally {
            socket.close();
        }

    }
}

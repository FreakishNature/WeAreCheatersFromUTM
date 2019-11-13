package com.UDP;

import com.UDP.processors.PacketProcessor;
import com.UDP.processors.ResponseProcessor;
import com.UDP.models.ReqRespEntity;
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
    private ResponseProcessor requestProcessor;

    static public KeyStorage keyStorage = new KeyStorage(
            ServerKeys.rsaPrivateKey,
            ServerKeys.dsaPrivateKey,
            ClientKeys.rsaPublicKey,
            ClientKeys.dsaPublicKey,
            ServerKeys.aesKey
    );

    public static String errorMessage = "Message was send badly";
    private DatagramSocket socket;

    public Server(int port, ResponseProcessor requestProcessor) throws SocketException {
        socket = new DatagramSocket(port);
        this.requestProcessor = requestProcessor;
    }

    public void run() {
        logger.info("Server started");
        try {
            while (true) {
                byte[] buf = new byte[2048];

                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);

                ReqRespEntity reqRespEntity;
                try {
                    reqRespEntity = PacketProcessor.receiveAndDecrypt(socket, packet, keyStorage);

                    PacketProcessor.validateAndSend(
                            socket,
                            reqRespEntity,
                            packet,
                            requestProcessor.processResponse(reqRespEntity.getMessage()),
                            keyStorage
                    );
                } catch (IOException | NoSuchAlgorithmException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } finally {
            socket.close();
        }

    }
}

package com.UDP;

import com.UDP.models.ReqRespEntity;
import com.UDP.processors.PacketProcessor;
import com.UDP.processors.ResponseProcessor;
import com.security.keys.KeyStorage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class UdpServer {
    private static UdpServer server;
    private ResponseProcessor requestProcessor;
    Logger logger = Logger.getLogger(this.getClass().getName());
    KeyStorage keyStorage;

    {
        try {
            keyStorage = KeyStorage.getKeys("UdpServer");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String errorMessage = "Message was send badly";
    private DatagramSocket socket;

    public static synchronized UdpServer getInstance(int port, ResponseProcessor requestProcessor) throws SocketException {
            if (server == null) {
            server = new UdpServer(port, requestProcessor);
        }
        return server;
    }

    private UdpServer(int port, ResponseProcessor requestProcessor) throws SocketException {
        socket = new DatagramSocket(port);
        this.requestProcessor = requestProcessor;
    }

    public void run() {
        logger.info("UdpServer started");
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

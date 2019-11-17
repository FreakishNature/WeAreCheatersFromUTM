package com.UDP;

import com.UDP.exceptions.ServerException;
import com.UDP.models.ReqRespEntity;
import com.UDP.processors.PacketProcessor;
import com.security.keys.ClientKeys;
import com.security.keys.KeyStorage;
import com.security.keys.ServerKeys;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Client {
    Logger logger = Logger.getLogger(this.getClass().getName());
    static public KeyStorage keyStorage = new KeyStorage(
            ClientKeys.rsaPrivateKey,
            ClientKeys.dsaPrivateKey,
            ServerKeys.rsaPublicKey,
            ServerKeys.dsaPublicKey,
            ClientKeys.aesKey
    );

    private DatagramSocket socket;
    private InetAddress address;
    int port;

    public Client(String host, int port) throws SocketException, UnknownHostException {
        this.port = port;
        socket = new DatagramSocket();
        address = InetAddress.getByName(host);
    }

    public String send(String msg, int attempt) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {

        byte[] buf = PacketProcessor.preparePacket(msg, attempt, keyStorage);

        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, this.port);

        logger.info("Sending packet to server");
        PacketProcessor.sendPacketByChunks(packet, socket, buf);
        logger.info("Waiting for server response");
        String responseMsg;

        try {

            ReqRespEntity reqRespEntity = PacketProcessor.receiveAndDecrypt(socket, packet, keyStorage);
            logger.info("Response received");
            responseMsg = reqRespEntity.getMessage();

            if (reqRespEntity.getMessage().equals(Server.errorMessage) || PacketProcessor.validatePacket(reqRespEntity)) {
                throw ServerException.receivingResponseException();
            }

        } catch (ServerException e) {
            logger.warning(e.getMessage() + " Attempt : " + attempt);
            if (attempt >= 5 || attempt < 0) {
                return e.getMessage();
            }
            responseMsg = send(msg, attempt + 1);
        }


        return responseMsg;
    }

}

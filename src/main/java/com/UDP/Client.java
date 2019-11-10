package com.UDP;

import com.UDP.exceptions.ServerException;
import com.UDP.processors.PacketProcessor;
import com.UDP.models.Request;
import com.UDP.processors.RequestProcessor;
import com.UDP.processors.ResponseProcessor;
import com.security.keys.ClientKeys;
import com.security.keys.KeyStorage;
import com.security.keys.ServerKeys;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
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
        socket.send(packet);

        logger.info("Waiting for server response");
        String responseMsg;

        try {

            Request request = PacketProcessor.receiveAndDecrypt(socket, packet, buf, keyStorage);
            logger.info("Response received");
            responseMsg = request.getMessage();

            if (request.getMessage().equals(Server.errorMessage) || !PacketProcessor.validatePacket(request)) {
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

    public static void main(String[] args) {

//        Arrays.asList(bytes)
//
//        System.out.println(l.subList(0,1));
    }
}

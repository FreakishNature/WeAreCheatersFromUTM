package com.UDP;

import com.UDP.exceptions.ServerException;
import com.UDP.models.ReqRespEntity;
import com.UDP.processors.PacketProcessor;
import com.security.keys.KeyStorage;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@NoArgsConstructor
public class UdpClient implements Client{
    Logger logger = Logger.getLogger(this.getClass().getName());
    KeyStorage keyStorage = KeyStorage.getKeys("AppClient");

    private DatagramSocket socket;
    private InetAddress address;
    int port;

    public UdpClient(String host, int port) throws SocketException, UnknownHostException {
        this.port = port;
        socket = new DatagramSocket();
        address = InetAddress.getByName(host);
    }

    public String send(String msg, int attempt) {
        String responseMsg = "Default message";

        try {
            byte[] buf = PacketProcessor.preparePacket(msg, attempt, keyStorage);

            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length, address, this.port);

            logger.info("Sending packet to server");
            PacketProcessor.sendPacketByChunks(packet, socket, buf);
            logger.info("Waiting for server response");

            try {

                ReqRespEntity reqRespEntity = PacketProcessor.receiveAndDecrypt(socket, packet, keyStorage);
                logger.info("Response received");
                responseMsg = reqRespEntity.getMessage();

                if (reqRespEntity.getMessage().equals(UdpServer.errorMessage) || !PacketProcessor.validatePacket(reqRespEntity)) {
                    throw ServerException.receivingResponseException();
                }

            } catch (ServerException e) {
                logger.warning(e.getMessage() + " Attempt : " + attempt);
                if (attempt >= 5 || attempt < 0) {
                    return e.getMessage();
                }
                responseMsg = send(msg, attempt + 1);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return responseMsg;

    }

}

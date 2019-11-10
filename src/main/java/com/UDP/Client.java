package com.UDP;

import com.UDP.exceptions.ServerException;
import com.model.Request;
import com.security.ClientKeys;
import com.security.Keys;
import com.security.PacketProcessor;
import com.security.ServerKeys;

import java.io.IOException;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Client {
    Logger logger = Logger.getLogger(Client.class.getName());
    static public Keys keys = new Keys(
            ClientKeys.rsaPrivateKey,
            ClientKeys.dsaPrivateKey,
            ServerKeys.rsaPublicKey,
            ServerKeys.dsaPublicKey,
            ClientKeys.aesKey
    );

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {
        Client client = new Client("localhost",4000);
        System.out.println(client.sendEcho("hello",1));
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

    public String sendEcho(String msg,int attempt) throws IOException, NoSuchAlgorithmException, ClassNotFoundException {

        buf = PacketProcessor.preparePacket(msg,attempt,keys);

        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, this.port);

        logger.info("Sending packet to server");
        socket.send(packet);

        logger.info("Waiting for server response");
        String responseMsg;

        try{
            Request request = PacketProcessor.receiveAndDecrypt(socket,packet,buf,keys);
            logger.info("Response received");
            responseMsg = request.getMessage();

            if(request.getMessage().equals(Server.errorMessage) || !PacketProcessor.validatePacket(request)){
                throw ServerException.receivingResponseException();
            }

        }catch (ServerException e){
            logger.warning(e.getMessage() + " Attempt : " + attempt);
            if(attempt >= 5 || attempt < 0){
                return e.getMessage();
            }
            responseMsg = sendEcho(msg,attempt + 1);
        }


        return responseMsg;
    }

    public void close() {
        socket.close();
    }
}

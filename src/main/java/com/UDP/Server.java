package com.UDP;

import com.security.ClientKeys;
import com.security.Keys;
import com.security.ServerKeys;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;

public class Server {

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

    public Server(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }


    public void run() throws IOException, NoSuchAlgorithmException {
    }

    private String processRequestMessage(String message) {
        return "good response";
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        new Server(4000).run();
    }
}

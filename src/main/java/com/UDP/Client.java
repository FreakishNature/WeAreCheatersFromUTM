package com.UDP;

import com.security.ClientKeys;
import com.security.Keys;
import com.security.ServerKeys;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class Client {
    static public Keys keys = new Keys(
            ClientKeys.rsaPrivateKey,
            ClientKeys.dsaPrivateKey,
            ServerKeys.rsaPublicKey,
            ServerKeys.dsaPublicKey,
            ClientKeys.aesKey
    );

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Client client = new Client("localhost",4000);
        System.out.println(client.sendEcho("hello",0));
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

    public String sendEcho(String msg,int attempt) throws IOException, NoSuchAlgorithmException {
        return "";
    }

    public void close() {
        socket.close();
    }
}

package com.UDP;

public abstract class ClientDecorator implements Client {
    Client client;

    public ClientDecorator(Client client){
        this.client = client;
    }

    public String send (String msg, int attempt) {
       return client.send(msg, attempt);
    }
}

package com.security.keys;

import lombok.Getter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
public class KeyStorage {
    private static ClientKeys clientKeys;
    private static ServerKeys serverKeys;

    static {
        try {
            clientKeys = new ClientKeys();
            serverKeys = new ServerKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PrivateKey rsaPrivateKey;
    private PrivateKey dsaPrivateKey;
    private PublicKey rsaPublicKey;
    private PublicKey dsaPublicKey;
    private SecretKey aesKey;

    private KeyStorage(PrivateKey rsaPrivateKey, PrivateKey dsaPrivateKey, PublicKey rsaPublicKey, PublicKey dsaPublicKey, SecretKey aesKey) {
        this.rsaPrivateKey = rsaPrivateKey;
        this.dsaPrivateKey = dsaPrivateKey;
        this.rsaPublicKey = rsaPublicKey;
        this.dsaPublicKey = dsaPublicKey;
        this.aesKey = aesKey;
    }

    //Factory
    public static KeyStorage getKeys(String type) throws IOException {
        switch (type) {
            case "AppClient":
                return new KeyStorage(
                        clientKeys.getRsaPrivateKey(),
                        clientKeys.getDsaPrivateKey(),
                        serverKeys.getRsaPublicKey(),
                        serverKeys.getDsaPublicKey(),
                        clientKeys.getAesKey()
                );
            case "UdpServer":
                return new KeyStorage(
                        serverKeys.getRsaPrivateKey(),
                        serverKeys.getDsaPrivateKey(),
                        clientKeys.getRsaPublicKey(),
                        clientKeys.getDsaPublicKey(),
                        serverKeys.getAesKey()
                );
        }

        throw new IOException("No such option");
    }

}

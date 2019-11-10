package com.security;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ClientKeys {
    static public PrivateKey rsaPrivateKey;
    static public PrivateKey dsaPrivateKey;
    static public PublicKey rsaPublicKey;
    static public PublicKey dsaPublicKey;
    static public SecretKey aesKey;


    static {
        try {
            rsaPrivateKey = RSA.getKeyPair().getPrivate();
            rsaPublicKey = RSA.getKeyPair().getPublic();
            dsaPrivateKey = DSA.generateKeyPair(999).getPrivate();
            dsaPublicKey = DSA.generateKeyPair(999).getPublic();
            aesKey = AES.getSecretKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

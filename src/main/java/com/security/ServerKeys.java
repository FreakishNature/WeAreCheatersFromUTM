package com.security;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class ServerKeys {
    static public PrivateKey rsaPrivateKey;
    static public PrivateKey dsaPrivateKey;
    static public PublicKey rsaPublicKey;
    static public PublicKey dsaPublicKey;
    static public SecretKey aesKey;


    static {
        try {
            rsaPrivateKey = RSA.getKeyPair().getPrivate();
            dsaPrivateKey = DSA.generateKeyPair(999).getPrivate();

            rsaPublicKey = RSA.getKeyPair().getPublic();
            dsaPublicKey = DSA.generateKeyPair(999).getPublic();

            aesKey = AES.getSecretKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

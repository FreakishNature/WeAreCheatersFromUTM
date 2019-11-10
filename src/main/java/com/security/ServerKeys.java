package com.security;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ServerKeys {
    static public PrivateKey rsaPrivateKey;
    static public PrivateKey dsaPrivateKey;
    static public PublicKey rsaPublicKey;
    static public PublicKey dsaPublicKey;
    static public SecretKey aesKey;


    static {
        try {
            rsaPrivateKey = RSA.getKeyPair("/rsaServerKeys.jks", "s3cr3t", "s3cr3t", "mykey").getPrivate();
            dsaPrivateKey = DSA.generateKeyPair(900).getPrivate();

            rsaPublicKey = RSA.getKeyPair("/rsaServerKeys.jks", "s3cr3t", "s3cr3t", "mykey").getPublic();
            dsaPublicKey = DSA.generateKeyPair(900).getPublic();

            aesKey = AES.getSecretKey("/aesServerKey.jck", "mystorepass", "mykeypass", "myseckey");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

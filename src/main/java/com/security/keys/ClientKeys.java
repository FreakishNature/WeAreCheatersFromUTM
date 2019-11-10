package com.security.keys;

import com.security.algorithms.AES;
import com.security.algorithms.DSA;
import com.security.algorithms.RSA;

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
            rsaPrivateKey = RSA.getKeyPair("/rsaClientKeys.jks", "s3cr3tclient", "s3cr3tclient", "mykeyclient").getPrivate();
            dsaPrivateKey = DSA.generateKeyPair(1000).getPrivate();

            rsaPublicKey = RSA.getKeyPair("/rsaClientKeys.jks", "s3cr3tclient", "s3cr3tclient", "mykeyclient").getPublic();
            dsaPublicKey = DSA.generateKeyPair(1000).getPublic();

            aesKey = AES.getSecretKey("/aesClientKey.jck", "mystorepassclient", "mykeypassclient", "myseckeyclient");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

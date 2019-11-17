package com.security.keys;

import com.security.algorithms.AES;
import com.security.algorithms.DSA;
import com.security.algorithms.RSA;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
public class ClientKeys {
    private PrivateKey rsaPrivateKey;
    private PrivateKey dsaPrivateKey;
    private PublicKey rsaPublicKey;
    private PublicKey dsaPublicKey;
    private SecretKey aesKey;

    ClientKeys() throws Exception {
        rsaPrivateKey = RSA.getKeyPair("/rsaClientKeys.jks", "s3cr3tclient", "s3cr3tclient", "mykeyclient").getPrivate();
        dsaPrivateKey = DSA.generateKeyPair(1000).getPrivate();

        rsaPublicKey = RSA.getKeyPair("/rsaClientKeys.jks", "s3cr3tclient", "s3cr3tclient", "mykeyclient").getPublic();
        dsaPublicKey = DSA.generateKeyPair(1000).getPublic();

        aesKey = AES.getSecretKey("/aesClientKey.jck", "mystorepassclient", "mykeypassclient", "myseckeyclient");
    }

}

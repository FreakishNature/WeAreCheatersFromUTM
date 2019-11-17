package com.security.keys;

import com.security.algorithms.AES;
import com.security.algorithms.DSA;
import com.security.algorithms.RSA;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
public class ServerKeys {
    private PrivateKey rsaPrivateKey;
    private PrivateKey dsaPrivateKey;
    private PublicKey rsaPublicKey;
    private PublicKey dsaPublicKey;
    private SecretKey aesKey;


    ServerKeys() throws Exception {
        rsaPrivateKey = RSA.getKeyPair("/rsaServerKeys.jks", "s3cr3t", "s3cr3t", "mykey").getPrivate();
        dsaPrivateKey = DSA.generateKeyPair(900).getPrivate();

        rsaPublicKey = RSA.getKeyPair("/rsaServerKeys.jks", "s3cr3t", "s3cr3t", "mykey").getPublic();
        dsaPublicKey = DSA.generateKeyPair(900).getPublic();

        aesKey = AES.getSecretKey("/aesServerKey.jck", "mystorepass", "mykeypass", "myseckey");
    }

}

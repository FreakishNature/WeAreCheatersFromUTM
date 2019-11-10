package com.security.keys;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyStorage {
    PrivateKey rsaPrivateKey;
    PrivateKey dsaPrivateKey;
    PublicKey rsaPublicKey;
    PublicKey dsaPublicKey;
    SecretKey aesKey;

    public KeyStorage(PrivateKey rsaPrivateKey, PrivateKey dsaPrivateKey, PublicKey rsaPublicKey, PublicKey dsaPublicKey, SecretKey aesKey) {
        this.rsaPrivateKey = rsaPrivateKey;
        this.dsaPrivateKey = dsaPrivateKey;
        this.rsaPublicKey = rsaPublicKey;
        this.dsaPublicKey = dsaPublicKey;
        this.aesKey = aesKey;
    }

    public PrivateKey getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public void setRsaPrivateKey(PrivateKey rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public PrivateKey getDsaPrivateKey() {
        return dsaPrivateKey;
    }

    public void setDsaPrivateKey(PrivateKey dsaPrivateKey) {
        this.dsaPrivateKey = dsaPrivateKey;
    }

    public PublicKey getRsaPublicKey() {
        return rsaPublicKey;
    }

    public void setRsaPublicKey(PublicKey rsaPublicKey) {
        this.rsaPublicKey = rsaPublicKey;
    }

    public PublicKey getDsaPublicKey() {
        return dsaPublicKey;
    }

    public void setDsaPublicKey(PublicKey dsaPublicKey) {
        this.dsaPublicKey = dsaPublicKey;
    }

    public SecretKey getAesKey() {
        return aesKey;
    }

    public void setAesKey(SecretKey aesKey) {
        this.aesKey = aesKey;
    }
}

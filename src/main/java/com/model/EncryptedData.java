package com.model;

import java.io.Serializable;

public class EncryptedData implements Serializable {
    String cipherText;
    byte[] signature;
    String aesKeyEncrypted;

    public EncryptedData(String encryptedData, byte[] signature, String aesKeyEncrypted) {
        this.cipherText = encryptedData;
        this.signature = signature;
        this.aesKeyEncrypted = aesKeyEncrypted;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public String getAesKeyEncrypted() {
        return aesKeyEncrypted;
    }

    public void setAesKeyEncrypted(String aesKeyEncrypted) {
        this.aesKeyEncrypted = aesKeyEncrypted;
    }
}

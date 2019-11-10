package com.security;

import com.security.algorithms.AES;
import com.security.algorithms.DSA;
import com.security.algorithms.RSA;
import com.security.keys.ClientKeys;
import com.security.keys.ServerKeys;
import com.security.models.EncryptedData;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class SecurityManager {

    static public String getHashSum(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(str.getBytes());
        BigInteger bi = new BigInteger(1, hash);

        return bi.toString(16);
    }

    public static EncryptedData encrypt(String s, SecretKey aesKey, PublicKey rsaPublicKey, PrivateKey dsaPrivateKey) {
        try {
            String cipherText = AES.encrypt(s, aesKey);
            String aesKeyEncrypted = RSA.encrypt(
                    Base64.getEncoder().encodeToString(aesKey.getEncoded()),
                    rsaPublicKey
            );
            byte[] signature = DSA.sign(cipherText, dsaPrivateKey);

            return new EncryptedData(cipherText, signature, aesKeyEncrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decrypt(EncryptedData encryptedData, PrivateKey rsaPrivateKey, PublicKey dsaPublicKey) {
        try {
            if (DSA.verify(encryptedData.getCipherText(), dsaPublicKey, encryptedData.getSignature())) {
                String aesKeyDecrypted = RSA.decrypt(encryptedData.getAesKeyEncrypted(), rsaPrivateKey);

                byte[] aesKeyDecoded = Base64.getDecoder().decode(aesKeyDecrypted);
                SecretKey aesKey = new SecretKeySpec(aesKeyDecoded, 0, aesKeyDecoded.length, "AES");

                return AES.decrypt(encryptedData.getCipherText(), aesKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String a = "test server";

        EncryptedData cipherText = encrypt(a, ServerKeys.aesKey, ServerKeys.rsaPublicKey, ServerKeys.dsaPrivateKey);
        String plainText = decrypt(cipherText, ClientKeys.rsaPrivateKey, ClientKeys.dsaPublicKey);
        System.out.println(plainText);

        a = "test client";

        cipherText = encrypt(a, ClientKeys.aesKey, ClientKeys.rsaPublicKey, ClientKeys.dsaPrivateKey);
        plainText = decrypt(cipherText, ServerKeys.rsaPrivateKey, ServerKeys.dsaPublicKey);
        System.out.println(plainText);
    }

}


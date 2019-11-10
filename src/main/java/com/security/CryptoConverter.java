package com.security;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class CryptoConverter {
    static PrivateKey rsaPrivateKey;
    static PrivateKey dsaPrivateKey;
    static PublicKey rsaPublicKey;
    static PublicKey dsaPublicKey;
    static SecretKey aesKey;
    static String aesKeyEncrypted;
    static byte[] signature;

    static public String getHashSum(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(str.getBytes());
        BigInteger bi = new BigInteger(1, hash);

        return bi.toString(16);
    }

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

    public static String encrypt(String s, SecretKey aesKey, PublicKey rsaPublicKey, PrivateKey dsaPrivateKey) {
        try {
            String cipherText = AES.encrypt(s, aesKey);
            aesKeyEncrypted = RSA.encrypt(
                    Base64.getEncoder().encodeToString(aesKey.getEncoded()),
                    rsaPublicKey
            );
            signature = DSA.sign(cipherText, dsaPrivateKey);
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decrypt(String s, String aesKeyEncrypted, PrivateKey rsaPrivateKey, PublicKey dsaPublicKey, byte[] signature) {
        try {
            if (DSA.verify(s, dsaPublicKey, signature)) {
                String aesKeyDecrypted = RSA.decrypt(aesKeyEncrypted, rsaPrivateKey);

                byte[] aesKeyDecoded = Base64.getDecoder().decode(aesKeyDecrypted);
                SecretKey aesKey = new SecretKeySpec(aesKeyDecoded, 0, aesKeyDecoded.length, "AES");

                return AES.decrypt(s, aesKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String s) {
        return "11";
    }

    public static String encrypt(String s) {
        return "22";
    }

    public static void main(String[] args) {
        String a = "test";

        String cipherText = encrypt(a, aesKey, rsaPublicKey, dsaPrivateKey);

        String plainText = decrypt(cipherText, aesKeyEncrypted, rsaPrivateKey, dsaPublicKey, signature);
        System.out.println(plainText);
    }

}


package com.security;

import com.model.EncryptedData;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class CryptoConverter {
//    private static String aesKeyEncrypted;
//    private static byte[] signature;

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

            return new EncryptedData(cipherText,signature,aesKeyEncrypted);
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

    public static String decrypt(String s) {
        return "11";
    }

    public static String encrypt(String s) {
        return "22";
    }

    public static void main(String[] args) {
        String a = "test";

//        String cipherText = encrypt(a, aesKey, rsaPublicKey, dsaPrivateKey);
//
//        String plainText = decrypt(cipherText, aesKeyEncrypted, rsaPrivateKey, dsaPublicKey, signature);
//        System.out.println(plainText);
    }

}


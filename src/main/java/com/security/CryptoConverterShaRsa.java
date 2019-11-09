package com.security;

import java.security.KeyPair;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.math.*;

public class CryptoConverterShaRsa {
        static KeyPair pair;

        static public String getHashSum(String str) throws NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(str.getBytes());
            BigInteger bi = new BigInteger(1, hash);

            return bi.toString(16);
        }

        static {
            try {
                pair = CryptoShaRsa.getKeyPairFromKeyStore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String encrypt(String s) {
            try {
                return CryptoShaRsa.encrypt(s, pair.getPublic());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static String decrypt(String s) {
            try {
                return CryptoShaRsa.decrypt(s, pair.getPrivate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


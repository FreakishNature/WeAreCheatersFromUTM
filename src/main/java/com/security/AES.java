package com.security;


import javax.crypto.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.KeyStore.SecretKeyEntry;
import java.security.cert.CertificateException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * keytool command:
 * keytool -genseckey -keyalg AES -alias myseckey -keysize 256 -keypass mykeypass -storetype jceks -keystore mystore.jck -storepass mystorepass
 */

public class AES {

    public static SecretKey getSecretKey() throws KeyStoreException,
            IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableEntryException {

        InputStream ins = AES.class.getResourceAsStream("/mystore.jck");

        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(ins, "mystorepass".toCharArray());

        SecretKeyEntry entry = (SecretKeyEntry) ks.getEntry("myseckey",
                new KeyStore.PasswordProtection("mykeypass".toCharArray()));

        return entry.getSecretKey();
    }

    public static String decrypt(final String cipherText, final SecretKey myDesKey)
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {

        byte[] bytes = Base64.getDecoder().decode(cipherText);

        Cipher decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, myDesKey);

        return new String(decryptCipher.doFinal(bytes), UTF_8);
    }

    public static String encrypt(final String secretText, final SecretKey myDesKey)
            throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {

        Cipher decryptCipher = Cipher.getInstance("AES");
        decryptCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

        byte[] cipherText = decryptCipher.doFinal(secretText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

}
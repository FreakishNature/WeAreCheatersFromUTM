package com.security.algorithms;

import java.security.*;
import java.util.Base64;


public class DSA {
    public static KeyPair generateKeyPair(long seed) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("DSA");
        SecureRandom rng = SecureRandom.getInstance("SHA1PRNG", "SUN");
        rng.setSeed(seed);
        keyGenerator.initialize(1024, rng);

        return (keyGenerator.generateKeyPair());
    }

    public static byte[] sign(String message, PrivateKey key) throws Exception {
        byte[] data = Base64.getDecoder().decode(message);
        Signature signer = Signature.getInstance("SHA1withDSA");
        signer.initSign(key);
        signer.update(data);
        return (signer.sign());
    }

    public static boolean verify(String message, PublicKey key, byte[] sig) throws Exception {
        byte[] data = Base64.getDecoder().decode(message);
        Signature signer = Signature.getInstance("SHA1withDSA");
        signer.initVerify(key);
        signer.update(data);
        return (signer.verify(sig));

    }

}
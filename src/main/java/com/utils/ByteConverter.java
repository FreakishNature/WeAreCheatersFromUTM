package com.utils;

import java.nio.charset.StandardCharsets;

public class ByteConverter {
    public static byte[] toByteArray(String s) {
        if(s.length() < 2048){
            return (s + new String(new char[(2048 - s.length())])).getBytes(StandardCharsets.UTF_8);
        } else {
            return s.getBytes(StandardCharsets.UTF_8);
        }
    }

    public static String toString(byte[] b) {
        return new String(b, StandardCharsets.UTF_8);
    }
}

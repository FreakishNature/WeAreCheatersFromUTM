package com.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedData implements Serializable {
    String cipherText;
    byte[] signature;
    String aesKeyEncrypted;
}

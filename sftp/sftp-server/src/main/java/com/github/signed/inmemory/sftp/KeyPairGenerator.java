package com.github.signed.inmemory.sftp;

import java.security.KeyPair;

import org.apache.sshd.common.util.SecurityUtils;

public class KeyPairGenerator {

    private String algorithm;
    private int keySize;

    public KeyPairGenerator() {
        dsa();
    }

    public KeyPairGenerator dsa() {
        algorithm = "DSA";
        return this;
    }

    public KeyPairGenerator rsa() {
        algorithm = "RSA";
        return this;
    }

    public KeyPairGenerator keySize(int keySize) {
        this.keySize = keySize;
        return this;
    }


    public KeyPair generateKeyPair() {
        try {
            java.security.KeyPairGenerator generator = SecurityUtils.getKeyPairGenerator(algorithm);
            if (keySize != 0) {
                generator.initialize(keySize);
            }
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

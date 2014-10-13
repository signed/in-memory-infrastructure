package com.github.signed.inmemory.sftp;

import java.security.KeyPair;

import org.apache.sshd.common.util.SecurityUtils;

public class KeyPairGenerator {

    private String algorithm;

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

    public KeyPair generateKeyPair() {
        try {
            java.security.KeyPairGenerator generator = SecurityUtils.getKeyPairGenerator(algorithm);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
package com.github.signed.inmemory.sftp.junit;

import java.security.PublicKey;

import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public class PublicKeyVerifier implements HostKeyVerifier {

    private final PublicKey publicKey;

    public PublicKeyVerifier(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean verify(String hostname, int port, PublicKey key) {
        return key.equals(publicKey);
    }
}

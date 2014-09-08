package com.github.signed.inmemory.sftp;

import java.security.PublicKey;

public class HostKey {

    private final PublicKey publicKey;

    public HostKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKey publicKey(){
        return publicKey;
    }
}

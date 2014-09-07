package com.github.signed.inmemory.sftp;

public class HostKey {

    private String fingerprint;

    public HostKey(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String fingerprint() {
        return fingerprint;
    }
}

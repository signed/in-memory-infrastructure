package com.github.signed.inmemory.sftp;

import java.security.PublicKey;

public class SftpUser {
    private final String login;
    private final String password;
    private final PublicKey publicKey;

    public SftpUser(String login, String password, PublicKey publicKey) {
        this.login = login;
        this.password = password;
        this.publicKey = publicKey;
    }

    public String login() {
        return login;
    }

    public String password(){
        return password;
    }

    public PublicKey publicKey(){
        return publicKey;
    }
}

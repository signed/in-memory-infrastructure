package com.github.signed.inmemory.sftp;

import java.security.PublicKey;
import java.util.Map;

import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;

class SftpUserBackedPublicKeyAuthenticator implements PublickeyAuthenticator {
    private final Map<String, SftpUser> dictionary;

    public SftpUserBackedPublicKeyAuthenticator(Map<String, SftpUser> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) {
        SftpUser sftpUser = dictionary.get(username);
        return null != sftpUser && key.equals(sftpUser.publicKey());
    }
}

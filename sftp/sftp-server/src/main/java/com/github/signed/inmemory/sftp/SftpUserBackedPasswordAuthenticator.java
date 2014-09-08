package com.github.signed.inmemory.sftp;

import java.util.Map;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

public class SftpUserBackedPasswordAuthenticator implements PasswordAuthenticator {
    private final Map<String, SftpUser> users;

    public SftpUserBackedPasswordAuthenticator(Map<String, SftpUser> users) {
        this.users = users;
    }

    public boolean authenticate(String username, String password, ServerSession session) {
        SftpUser user = users.get(username);
        return null != user && password.equals(user.password());
    }
}

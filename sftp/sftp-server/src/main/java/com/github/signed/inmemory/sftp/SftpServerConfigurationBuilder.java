package com.github.signed.inmemory.sftp;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.github.signed.inmemory.shared.configuration.ExplicitPort;
import com.github.signed.inmemory.shared.configuration.Port;
import com.github.signed.inmemory.shared.configuration.RandomPort;

public class SftpServerConfigurationBuilder {

    public static SftpServerConfigurationBuilder sftpServer() {
        return new SftpServerConfigurationBuilder();
    }

    private final List<SftpUser> users = new ArrayList<SftpUser>();
    private File userHomeDirectory;
    private Port port = RandomPort.AnyUserPort();

    public SftpServerConfigurationBuilder listenOnPort(int port) {
        return listenOnPort(new ExplicitPort(port));
    }

    public SftpServerConfigurationBuilder listenOnPort(Port port) {
        this.port = port;
        return this;
    }

    public SftpServerConfigurationBuilder userHomeDirectoryAt(File directory) {
        this.userHomeDirectory = directory;
        return this;
    }

    public SftpServerConfigurationBuilder registerAccountFor(String login, String password) {
        users.add(new SftpUser(login, password, null));
        return this;
    }

    public SftpServerConfigurationBuilder registerAccountFor(String login, PublicKey publicKey) {
        users.add(new SftpUser(login, null, publicKey));
        return this;
    }

    public SftpServerConfiguration build() {
        return new SftpServerConfiguration(userHomeDirectory, port, users);
    }
}

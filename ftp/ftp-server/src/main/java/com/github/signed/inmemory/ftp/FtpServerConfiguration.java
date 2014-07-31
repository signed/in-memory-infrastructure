package com.github.signed.inmemory.ftp;

import java.io.File;
import java.util.Collections;
import java.util.List;

import com.github.signed.inmemory.shared.configuration.Port;

public class FtpServerConfiguration {
    private final File rootDirectory;
    private final Port port;
    private final Iterable<FtpUser> users;

    public FtpServerConfiguration(File rootDirectory, Port port, List<FtpUser> users) {
        this.rootDirectory = rootDirectory;
        this.port = port;
        this.users = Collections.unmodifiableList(users);
    }

    public int port() {
        return port.port();
    }

    public File rootDirectory() {
        return rootDirectory;
    }

    public Iterable<FtpUser> users() {
        return users;
    }
}

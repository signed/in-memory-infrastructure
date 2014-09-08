package com.github.signed.inmemory.sftp;

import java.io.File;
import java.util.List;

import com.github.signed.inmemory.shared.configuration.Port;

public class SftpServerConfiguration {

    private final File userHomeDirectory;
    private final Port port;
    private final List<SftpUser> users;

    public SftpServerConfiguration(File userHomeDirectory, Port port, List<SftpUser> users) {
        this.userHomeDirectory = userHomeDirectory;
        this.port = port;
        this.users = users;
    }

    public int port() {
        return port.port();
    }

    public File userHomeDirectory(){
        return userHomeDirectory;
    }

    public Iterable<SftpUser> users(){
        return users;
    }
}

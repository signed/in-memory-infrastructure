package com.github.signed.inmemory.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.signed.inmemory.shared.configuration.ExplicitPort;
import com.github.signed.inmemory.shared.configuration.Port;
import com.github.signed.inmemory.shared.configuration.RandomUserPort;

public class FtpServerConfigurationBuilder {

    public static  FtpServerConfigurationBuilder ftpServer() {
        return new FtpServerConfigurationBuilder();
    }

    private final List<FtpUser> users = new ArrayList<FtpUser>();
    private File ftpRootDirectory;
    private Port port = new RandomUserPort();

    public FtpServerConfigurationBuilder listeningOnPort(int port) {
        this.port = new ExplicitPort(port);
        return this;
    }

    public FtpServerConfigurationBuilder rootDirectoryAt(File ftpRootDirectory) {
        this.ftpRootDirectory = ftpRootDirectory;
        return this;
    }

    public FtpServerConfigurationBuilder registerAccountFor(String login, String password) {
        users.add(new FtpUser(login, password));
        return this;
    }

    public FtpServerConfiguration build() {
        return new FtpServerConfiguration(ftpRootDirectory, port, users);
    }
}

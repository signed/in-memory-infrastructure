package com.github.signed.inmemory.ftp;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class FtpServerConfiguration {
    public final File rootDirectory;
    public final int port;
    public final Iterable<FtpUser> users;

    public FtpServerConfiguration(File rootDirectory, int port, List<FtpUser> users) {
        this.rootDirectory = rootDirectory;
        this.port = port;
        this.users = Collections.unmodifiableList(users);
    }
}

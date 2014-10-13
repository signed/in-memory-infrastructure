package com.github.signed.inmemory.ftp.junit;

import java.io.File;
import java.io.IOException;

import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import com.github.signed.inmemory.ftp.FtpServerConfiguration;
import com.github.signed.inmemory.ftp.FtpServerConfigurationBuilder;

public class FtpServer extends ExternalResource{
    private final TemporaryFolder ftpRoot = new TemporaryFolder();
    private final FtpServerConfigurationBuilder configurationBuilder;
    private com.github.signed.inmemory.ftp.FtpServer ftpServer;

    public int port;

    public FtpServer(FtpServerConfigurationBuilder configurationBuilder){
        this.configurationBuilder = configurationBuilder;
    }

    @Override
    protected void before() throws Throwable {
        start();
    }

    public void start() throws IOException {
        ftpRoot.create();
        FtpServerConfiguration configuration = configurationBuilder.rootDirectoryAt(ftpRoot.getRoot()).build();
        port = configuration.port();
        ftpServer = new com.github.signed.inmemory.ftp.FtpServer(configuration);
        ftpServer.start();
    }

    @Override
    protected void after() {
        stop();
    }

    public void stop() {
        ftpServer.stop();
        ftpRoot.delete();
    }

    public File fileUploadedBy(String username) {
        return ftpServer.filesUploadedBy(username).singleFile();
    }
}

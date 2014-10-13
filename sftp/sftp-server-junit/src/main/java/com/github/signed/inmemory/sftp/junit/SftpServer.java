package com.github.signed.inmemory.sftp.junit;

import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import com.github.signed.inmemory.sftp.HostKey;
import com.github.signed.inmemory.sftp.SftpServerConfiguration;
import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;
import com.github.signed.inmemory.shared.file.UploadedFiles;

public class SftpServer extends ExternalResource {
    private final TemporaryFolder ftpRoot = new TemporaryFolder();
    private final SftpServerConfigurationBuilder configurationBuilder;
    private SftpServerConfiguration configuration;
    private com.github.signed.inmemory.sftp.SftpServer sftpServer;

    public SftpServer(SftpServerConfigurationBuilder configurationBuilder) {
        this.configurationBuilder = configurationBuilder;
    }

    @Override
    protected void before() throws Throwable {
        start();
    }

    public void start() {
        try {
            ftpRoot.create();
            configuration = configurationBuilder.userHomeDirectoryAt(ftpRoot.getRoot()).build();
            sftpServer = new com.github.signed.inmemory.sftp.SftpServer(configuration);
            sftpServer.start();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void after() {
        stop();
    }

    public void stop() {
        sftpServer.stop();
        ftpRoot.delete();
    }


    public int port() {
        return configuration.port();
    }

    public HostKey hostKey() {
        return sftpServer.hostKey();
    }

    public UploadedFiles filesUploadedBy(String user) {
        throw new RuntimeException("not implemented for " + user);
    }
}

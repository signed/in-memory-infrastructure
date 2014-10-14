package com.github.signed.inmemory.sftp.junit;

import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import com.github.signed.inmemory.sftp.HostKey;
import com.github.signed.inmemory.sftp.SftpServerConfiguration;
import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;
import com.github.signed.inmemory.shared.file.UploadedFiles;

public class SftpServer extends ExternalResource {
    private final TemporaryFolder sftpRoot = new TemporaryFolder();
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
            sftpRoot.create();
            configuration = configurationBuilder.userHomeDirectoryAt(sftpRoot.getRoot()).build();
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
        sftpRoot.delete();
    }


    public int port() {
        return configuration.port();
    }

    public HostKey hostKey() {
        return sftpServer.hostKey();
    }

    public UploadedFiles filesUploadedBy(String user) {
        return sftpServer.filesUploadedBy(user);
    }
}

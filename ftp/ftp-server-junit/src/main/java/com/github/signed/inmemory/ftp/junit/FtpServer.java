package com.github.signed.inmemory.ftp.junit;

import com.github.signed.inmemory.ftp.FtpServerConfiguration;
import com.github.signed.inmemory.ftp.FtpServerConfigurationBuilder;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

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
        ftpRoot.create();
        FtpServerConfiguration configuration = configurationBuilder.rootDirectoryAt(ftpRoot.getRoot()).build();
        port = configuration.port;
        ftpServer = new com.github.signed.inmemory.ftp.FtpServer(configuration);
        ftpServer.start();
    }

    @Override
    protected void after() {
        ftpServer.stop();
        ftpRoot.delete();
    }
}

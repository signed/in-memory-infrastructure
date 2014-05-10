package com.github.signed.inmemory.ftp.junit;

import com.github.signed.inmemory.ftp.FtpServerConfigurationBuilder;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Rule;
import org.junit.Test;

import java.net.InetAddress;

import static org.hamcrest.MatcherAssert.assertThat;

public class FtpServer_Test {

    @Rule
    public FtpServer ftpServer = new FtpServer(new FtpServerConfigurationBuilder().listeningOnPort(10021).registerAccountFor("bob", "secret"));

    @Test
    public void startFtpServerSoClientsCanConnect() throws Exception {
        FTPClient client = new FTPClient();
        client.connect(InetAddress.getLocalHost(), ftpServer.port);
        client.login("bob", "secret");

        int replyCode = client.getReplyCode();

        client.logout();
        client.disconnect();
        assertThat("connect did not completed successfull", FTPReply.isPositiveCompletion(replyCode));
    }
}
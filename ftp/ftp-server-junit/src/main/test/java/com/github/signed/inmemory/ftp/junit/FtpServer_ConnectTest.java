package com.github.signed.inmemory.ftp.junit;

import static org.hamcrest.MatcherAssert.assertThat;

import java.net.InetAddress;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.ClassRule;
import org.junit.Test;

import com.github.signed.inmemory.ftp.FtpServerConfigurationBuilder;

public class FtpServer_ConnectTest {

    @ClassRule
    public static FtpServer ftpServer = new FtpServer(new FtpServerConfigurationBuilder().listeningOnPort(10021).registerAccountFor("bob", "secret"));

    @Test
    public void startFtpServerSoClientsCanConnect() throws Exception {
        FTPClient client = new FTPClient();
        client.connect(InetAddress.getLocalHost(), ftpServer.port);
        client.login("bob", "secret");

        int replyCode = client.getReplyCode();

        client.logout();
        client.disconnect();
        assertThat("connect did not completed successful", FTPReply.isPositiveCompletion(replyCode));
    }
}
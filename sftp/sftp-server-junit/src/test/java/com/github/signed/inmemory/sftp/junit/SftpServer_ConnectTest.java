package com.github.signed.inmemory.sftp.junit;

import org.junit.Rule;
import org.junit.Test;

import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;

import net.schmizz.sshj.SSHClient;

public class SftpServer_ConnectTest {


    private final SftpServerConfigurationBuilder configurationBuilder = SftpServerConfigurationBuilder.sftpServer();


    @Rule
    public SftpServer sftpServer = new SftpServer(configurationBuilder);

    @Test
    public void connectToTheServer() throws Exception {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(sftpServer.hostKey().fingerprint());
        ssh.connect("localhost", sftpServer.port());
        try {
            ssh.authPassword("signed", "secret");
            ssh.newSFTPClient().ls("/");
        } finally {
            ssh.disconnect();
        }
    }
}
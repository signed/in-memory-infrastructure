package com.github.signed.inmemory.sftp.junit;

import java.security.PublicKey;

import org.junit.Rule;
import org.junit.Test;

import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public class SftpServer_ConnectTest {


    private final SftpServerConfigurationBuilder configurationBuilder = SftpServerConfigurationBuilder.sftpServer();

    @Rule
    public SftpServer sftpServer = new SftpServer(configurationBuilder);

    @Test
    public void connectToTheServer() throws Exception {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new HostKeyVerifier() {
            @Override
            public boolean verify(String hostname, int port, PublicKey key) {
                return key.equals(sftpServer.hostKey().publicKey());
            }
        });
        ssh.connect("localhost", sftpServer.port());
        try {
            ssh.authPassword("signed", "secret");
            ssh.newSFTPClient().ls("/");
        } finally {
            ssh.disconnect();
        }
    }
}
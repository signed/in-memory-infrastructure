package com.github.signed.inmemory.sftp.junit;

import java.security.KeyPair;

import org.junit.Rule;
import org.junit.Test;

import com.github.signed.inmemory.sftp.KeyPairGenerator;
import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.userauth.keyprovider.KeyPairWrapper;

public class SftpServer_ConnectTest {

    private KeyPair clientKeyPair = new KeyPairGenerator().rsa().generateKeyPair();

    private final SftpServerConfigurationBuilder configurationBuilder = SftpServerConfigurationBuilder.sftpServer()
            .registerAccountFor("user", "password")
            .registerAccountFor("sally", clientKeyPair.getPublic());

    @Rule
    public SftpServer sftpServer = new SftpServer(configurationBuilder);

    @Test
    public void loginWithUserAndPassword() throws Exception {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PublicKeyVerifier(sftpServer.hostKey().publicKey()));
        ssh.connect("localhost", sftpServer.port());
        try {
            ssh.authPassword("user", "password");
            ssh.newSFTPClient().ls("/");
        } finally {
            ssh.disconnect();
        }
    }

    @Test
    public void loginWithPublicKey() throws Exception {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PublicKeyVerifier(sftpServer.hostKey().publicKey()));

        ssh.connect("localhost", sftpServer.port());
        try {
            ssh.authPublickey("sally", new KeyPairWrapper(clientKeyPair));
            ssh.newSFTPClient().ls("/");
        } finally {
            ssh.disconnect();
        }
    }

}
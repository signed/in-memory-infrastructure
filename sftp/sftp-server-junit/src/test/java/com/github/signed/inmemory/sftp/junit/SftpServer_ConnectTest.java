package com.github.signed.inmemory.sftp.junit;

import java.security.KeyPair;

import org.junit.Rule;
import org.junit.Test;

import com.github.signed.inmemory.sftp.KeyPairGenerator;
import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;

public class SftpServer_ConnectTest {

    private KeyPair clientKeyPair = new KeyPairGenerator().rsa().generateKeyPair();

    private final SftpServerConfigurationBuilder configurationBuilder = SftpServerConfigurationBuilder.sftpServer()
            .registerAccountFor("user", "password")
            .registerAccountFor("sally", clientKeyPair.getPublic());

    @Rule
    public SftpServer sftpServer = new SftpServer(configurationBuilder);

    @Rule
    public final SftpClientBuilder clientBuilder = new SftpClientBuilder();

    @Test
    public void loginWithUserAndPassword() throws Exception {
        clientBuilder.configureSshClient(sftpServer);
        clientBuilder.loginAs("user", "password");
        clientBuilder.client().ls("/");
    }

    @Test
    public void loginWithPublicKey() throws Exception {
        clientBuilder.configureSshClient(sftpServer);
        clientBuilder.loginAs("sally", clientKeyPair);
        clientBuilder.client().ls("/");
    }

}
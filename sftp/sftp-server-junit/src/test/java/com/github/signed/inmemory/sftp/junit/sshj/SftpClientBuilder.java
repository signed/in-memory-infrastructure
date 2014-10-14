package com.github.signed.inmemory.sftp.junit.sshj;

import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

import org.junit.rules.ExternalResource;

import com.github.signed.inmemory.sftp.HostKey;
import com.github.signed.inmemory.sftp.junit.SftpServer;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.userauth.keyprovider.KeyPairWrapper;

public class SftpClientBuilder extends ExternalResource {

    private final List<SSHClient> createdClients = new ArrayList<SSHClient>();
    private int port;
    private HostKey hostKey;
    private String user;
    private String password;
    private KeyPair keyPair;

    public SftpClientBuilder connectTo(SftpServer sftpServer) throws IOException {
        hostKey = sftpServer.hostKey();
        port = sftpServer.port();
        return this;
    }

    public SftpClientBuilder loginAs(String user, String password) throws UserAuthException, TransportException {
        this.user = user;
        this.password = password;
        return this;
    }

    public SftpClientBuilder loginAs(String login, KeyPair keyPair) throws UserAuthException, TransportException {
        this.user = login;
        this.keyPair = keyPair;
        return this;
    }

    public SFTPClient client() throws IOException {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PublicKeyVerifier(hostKey.publicKey()));
        ssh.connect("localhost", port);

        createdClients.add(ssh);

        if (null == password) {
            ssh.authPublickey(user, new KeyPairWrapper(keyPair));
        } else {
            ssh.authPassword(user, password);
        }

        return ssh.newSFTPClient();
    }

    @Override
    protected void after() {
        for (SSHClient createdClient : createdClients) {
            try {
                createdClient.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

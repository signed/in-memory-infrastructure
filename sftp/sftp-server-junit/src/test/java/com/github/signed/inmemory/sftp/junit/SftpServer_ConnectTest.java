package com.github.signed.inmemory.sftp.junit;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.userauth.method.AuthPassword;
import net.schmizz.sshj.userauth.password.PasswordFinder;
import net.schmizz.sshj.userauth.password.Resource;

public class SftpServer_ConnectTest {


    private final SftpServerConfigurationBuilder configurationBuilder = SftpServerConfigurationBuilder.sftpServer();


    @Rule
    public SftpServer sftpServer = new SftpServer(configurationBuilder);

    @Test
    @Ignore("not working right now")
    public void testName() throws Exception {
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(sftpServer.hostKey().fingerprint());
        ssh.connect("localhost", sftpServer.port());
        try {
            AuthPassword password = new AuthPassword(new PasswordFinder() {
                @Override
                public char[] reqPassword(Resource<?> resource) {
                    return "secret".toCharArray();
                }

                @Override
                public boolean shouldRetry(Resource<?> resource) {
                    return false;
                }
            });
            //ssh.auth("signed", password);
            ssh.authPassword("signed", "secret");
            ssh.authPublickey(System.getProperty("user.name"));
            final SFTPClient sftp = ssh.newSFTPClient();
            sftp.ls("/");
        } finally {
            ssh.disconnect();
        }
    }
}
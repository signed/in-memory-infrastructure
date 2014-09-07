package com.github.signed.inmemory.sftp;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthPassword;
import org.apache.sshd.server.auth.UserAuthPublicKey;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;

import com.github.signed.inmemory.shared.configuration.UserHomeCreator;

public class SftpServer {

    public static void main(String[] args) throws Exception {

        File serverRoot = new File("/tmp/sftp/");
        serverRoot.mkdirs();

        SftpServerConfiguration sftpServerConfiguration = SftpServerConfigurationBuilder.sftpServer().listeningOnPort(10022).userHomeDirectoryAt(serverRoot).build();

        SftpServer sftpServer = new SftpServer(sftpServerConfiguration);
        sftpServer.start();

        Thread.sleep(Long.MAX_VALUE);
        sftpServer.stop();
    }


    private final UserHomeCreator userHomeCreator;
    private final SftpServerConfiguration configuration;
    private SshServer sshd;


    public SftpServer(SftpServerConfiguration configuration) {
        userHomeCreator = new UserHomeCreator(configuration.userHomeDirectory());
        this.configuration = configuration;
    }

    public void start() {
        sshd = SshServer.setUpDefaultServer();
        sshd.setPort(configuration.port());

        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));

        sshd.setFileSystemFactory(new TestFileSystemFactory(configuration.userHomeDirectory()));

        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
        userAuthFactories.add(new UserAuthPassword.Factory());
        userAuthFactories.add(new UserAuthPublicKey.Factory());
        sshd.setUserAuthFactories(userAuthFactories);

        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
                return "signed".equals(username) && "secret".equals(password);
            }
        });

        sshd.setPublickeyAuthenticator(new PublickeyAuthenticator() {
            @Override
            public boolean authenticate(String username, PublicKey key, ServerSession session) {
                return true;
            }
        });

        sshd.setCommandFactory(new ScpCommandFactory());

        List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
        namedFactoryList.add(new SftpSubsystem.Factory());
        sshd.setSubsystemFactories(namedFactoryList);

        try {
            sshd.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HostKey hostKey() {
        return new HostKey("07:89:f0:7f:59:dc:bd:91:59:2a:3f:ff:3a:60:f9:40");
    }
}
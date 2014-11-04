package com.github.signed.inmemory.sftp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.KeyPairProvider;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.UserAuth;
import org.apache.sshd.server.auth.UserAuthPassword;
import org.apache.sshd.server.auth.UserAuthPublicKey;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.sftp.SftpSubsystem;

import com.github.signed.inmemory.shared.configuration.UserHomeCreator;
import com.github.signed.inmemory.shared.file.UploadedFiles;

public class SftpServer {

    public static void main(String[] args) throws Exception {

        File serverRoot = new File("/tmp/sftp/");
        serverRoot.mkdirs();

        SftpServerConfiguration sftpServerConfiguration = SftpServerConfigurationBuilder.sftpServer().listenOnPort(10022).userHomeDirectoryAt(serverRoot).build();

        SftpServer sftpServer = new SftpServer(sftpServerConfiguration);
        sftpServer.start();

        Thread.sleep(Long.MAX_VALUE);
        sftpServer.stop();
    }

    private static final KeyPairProvider keyPairProvider = InMemoryKeyPair.GenerateNewKeyPair();

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
        sshd.setKeyPairProvider(keyPairProvider);
        setupUserHomeDirectories();

        List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
        userAuthFactories.add(new UserAuthPassword.Factory());
        sshd.setPasswordAuthenticator(new SftpUserBackedPasswordAuthenticator(loginToUserDictionary()));
        userAuthFactories.add(new UserAuthPublicKey.Factory());
        sshd.setPublickeyAuthenticator(new SftpUserBackedPublicKeyAuthenticator(loginToUserDictionary()));
        sshd.setUserAuthFactories(userAuthFactories);
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
            //why do I need immediately?
            sshd.stop(true);
            //sshd.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HostKey hostKey() {
        return new HostKey(keyPairProvider.loadKey("ssh-dss").getPublic());
    }

    public UploadedFiles filesUploadedBy(String user) {
        File file = userHomeCreator.userHomeFor(user);
        return new UploadedFiles(file, user);
    }

    private void setupUserHomeDirectories() {
        FileSystemFactory fileSystemFactory = new SubDirectoryForEachUserSystemFactory(configuration.userHomeDirectory().toPath());
        for (SftpUser sftpUser : configuration.users()) {
            userHomeCreator.createUserHome(sftpUser.login());
        }
        sshd.setFileSystemFactory(fileSystemFactory);
    }

    private Map<String, SftpUser> loginToUserDictionary() {
        Map<String, SftpUser> users = new HashMap<String, SftpUser>();
        for (SftpUser sftpUser : configuration.users()) {
            users.put(sftpUser.login(), sftpUser);
        }
        return users;
    }
}
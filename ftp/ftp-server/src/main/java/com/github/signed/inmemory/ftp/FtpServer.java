package com.github.signed.inmemory.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import com.github.signed.inmemory.shared.configuration.UserHomeCreator;
import com.github.signed.inmemory.shared.file.UploadedFiles;

public class FtpServer {

    private org.apache.ftpserver.FtpServer server;
    private final FtpServerConfiguration configuration;
    private final UserHomeCreator userHomeCreator;

    public FtpServer(FtpServerConfiguration configuration) {
        userHomeCreator = new UserHomeCreator(configuration.rootDirectory());
        this.configuration = configuration;
    }

    public void start() {
        try {
            UserManager userManager = createUserManager();
            FtpServerFactory serverFactory = new FtpServerFactory();
            ListenerFactory listenerFactory = new ListenerFactory();
            listenerFactory.setPort(configuration.port());
            serverFactory.addListener("default", listenerFactory.createListener());
            serverFactory.setUserManager(userManager);
            server = serverFactory.createServer();
            server.start();
        } catch (FtpException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop();
    }

    public UploadedFiles filesUploadedBy(String username) {
        File file = userHomeCreator.userHomeFor(username);
        return new UploadedFiles(file, username);
    }

    private InMemoryUserManager createUserManager() throws FtpException {
        InMemoryUserManager userManager = new InMemoryUserManager();
        for (FtpUser user : configuration.users()) {
            BaseUser baseUser = new BaseUser();
            baseUser.setName(user.login);
            baseUser.setPassword(user.password);
            baseUser.setMaxIdleTime(0);
            baseUser.setHomeDirectory(userHomeCreator.createUserHome(user.login).getAbsolutePath());
            baseUser.setEnabled(true);

            baseUser.setAuthorities(createAuthorities());
            userManager.save(baseUser);
        }
        return userManager;
    }

    private List<Authority> createAuthorities() {
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new ConcurrentLoginPermission(0, 0));
        authorities.add(new WritePermission());
        authorities.add(new TransferRatePermission(0, 0));
        return authorities;
    }
}
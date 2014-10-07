package com.github.signed.inmemory.ftp;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
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

    public File fileUploadedBy(String username) {
        File file = userHomeCreator.userHomeFor(username);
        if (!file.isDirectory()) {
            throw new RuntimeException(String.format("I'm sorry, but I do not know <%s>.", username));
        }
        List<File> foundFiles = allUploadedFiles(file);
        if (foundFiles.isEmpty()) {
            throw new RuntimeException(String.format("I'm sorry, but <%s> did not upload any files.", username));
        }
        if (foundFiles.size() > 1) {
            throw new RuntimeException(String.format("Actually <%s> uploaded <%d> files. I'm sorry, but I do not know which one you want.", username, foundFiles.size()));
        }
        return foundFiles.get(0);
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

    private List<File> allUploadedFiles(File file) {
        final ArrayList<File> paths = new ArrayList<File>();

        try {
            Files.walkFileTree(file.toPath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    paths.add(file.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    throw new RuntimeException(exc);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return paths;
    }
}

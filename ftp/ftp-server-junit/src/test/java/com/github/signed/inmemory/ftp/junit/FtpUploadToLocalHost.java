package com.github.signed.inmemory.ftp.junit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;

import org.apache.commons.net.ftp.FTPClient;

public class FtpUploadToLocalHost {

    private final String user;
    private final String password;
    private final int port;

    public FtpUploadToLocalHost(String user, String password, int port) {
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public void upload(String remotePath, String content) {
        try {
            FTPClient client = new FTPClient();
            client.connect(InetAddress.getLocalHost(), port);
            client.login(user, password);

            int lastIndexOfPathSeparator = remotePath.lastIndexOf("/");
            String workingDirectory = ".";
            String filename = remotePath;

            if (-1 != lastIndexOfPathSeparator) {
                workingDirectory = remotePath.substring(0, lastIndexOfPathSeparator);
                filename = remotePath.substring(lastIndexOfPathSeparator + 1);
            }
            client.changeWorkingDirectory(workingDirectory);
            InputStream inputStream = new ByteArrayInputStream(content.getBytes());
            boolean success = client.storeFile(filename, inputStream);
            inputStream.close();

            client.logout();
            client.disconnect();
            if (!success) {
                throw new RuntimeException("failed to upload file");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

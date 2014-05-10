package com.github.signed.inmemory.ftp.junit;

import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;

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

            InputStream inputStream = new ByteArrayInputStream(content.getBytes());
            client.storeFile(remotePath, inputStream);
            inputStream.close();

            client.logout();
            client.disconnect();
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

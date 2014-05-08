package com.github.signed.inmemory.ftp;

import java.io.File;

public class UserHomeCreator {

    private final File temporaryFtpRoot;

    public UserHomeCreator(File temporaryFtpRoot) {
        this.temporaryFtpRoot = temporaryFtpRoot;
    }

    public File createUserHome(String userName) {
        File userHome = new File(temporaryFtpRoot, userName);
        userHome.mkdirs();
        return userHome;
    }
}

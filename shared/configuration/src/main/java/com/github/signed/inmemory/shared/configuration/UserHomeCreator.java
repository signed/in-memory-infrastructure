package com.github.signed.inmemory.shared.configuration;

import java.io.File;

public class UserHomeCreator {

    private final File temporaryFtpRoot;

    public UserHomeCreator(File temporaryFtpRoot) {
        this.temporaryFtpRoot = temporaryFtpRoot;
    }

    public File createUserHome(String userName) {
        File userHome = userHomeFor(userName);
        userHome.mkdirs();
        return userHome;
    }

    public File userHomeFor(String userName) {
        return new File(temporaryFtpRoot, userName);
    }
}

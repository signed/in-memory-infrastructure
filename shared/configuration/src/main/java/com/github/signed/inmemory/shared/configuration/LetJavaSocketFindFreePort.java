package com.github.signed.inmemory.shared.configuration;

public class LetJavaSocketFindFreePort implements Port{
    @Override
    public int port() {
        return 0;
    }
}

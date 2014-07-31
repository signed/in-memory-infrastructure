package com.github.signed.inmemory;

public class LetJavaSocketFindFreePort implements Port{
    @Override
    public int port() {
        return 0;
    }
}

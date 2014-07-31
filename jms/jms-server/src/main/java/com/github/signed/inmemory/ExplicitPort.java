package com.github.signed.inmemory;

public class ExplicitPort implements Port{

    private final int port;

    public ExplicitPort(int port) {
        this.port = port;
    }

    @Override
    public int port() {
        return port;
    }
}

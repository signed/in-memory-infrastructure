package com.github.signed.inmemory.shared.configuration;

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

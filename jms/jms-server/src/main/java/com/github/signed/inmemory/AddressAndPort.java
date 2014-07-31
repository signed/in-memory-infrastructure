package com.github.signed.inmemory;

public class AddressAndPort {

    private final Port port;

    public AddressAndPort(Port port) {
        this.port = port;
    }

    public String address(){
        return "localhost";
    }

    public int port() {
        return port.port();
    }
}

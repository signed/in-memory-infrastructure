package com.github.signed.inmemory.jms;

public class JmsServerConfigurationBuilder {

    public static JmsServerConfiguration anyJmsServerConfiguration() {
        JmsServerConfigurationBuilder builder = new JmsServerConfigurationBuilder();
        builder.bindTo("localhost", 5446);
        return builder.build();
    }

    private AddressAndPort host;

    public JmsServerConfigurationBuilder bindTo(String address, int port) {
        host = new AddressAndPort(address, port);
        return this;
    }

    public JmsServerConfiguration build() {
        return new JmsServerConfiguration(host);
    }
}

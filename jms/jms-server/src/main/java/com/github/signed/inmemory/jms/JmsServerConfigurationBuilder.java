package com.github.signed.inmemory.jms;

public class JmsServerConfigurationBuilder {
    public static JmsServerConfiguration anyJmsServerConfiguration() {
        AddressAndPort host = new AddressAndPort("localhost", 5446);
        return new JmsServerConfiguration(host);
    }
}

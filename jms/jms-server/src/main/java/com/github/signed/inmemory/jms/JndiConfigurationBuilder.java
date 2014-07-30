package com.github.signed.inmemory.jms;

public class JndiConfigurationBuilder {
    public static JndiConfiguration configuration() {
        AddressAndPort jndi = new AddressAndPort("localhost", 1099);
        AddressAndPort rmi = new AddressAndPort("localhost", 1098);

        return new JndiConfiguration(jndi, rmi);
    }
}

package com.github.signed.inmemory.jms;

public class JndiConfiguration {
    public final AddressAndPort jndi;
    public final AddressAndPort rmi;

    public JndiConfiguration(AddressAndPort jndi, AddressAndPort rmi) {
        this.jndi = jndi;
        this.rmi = rmi;
    }
}

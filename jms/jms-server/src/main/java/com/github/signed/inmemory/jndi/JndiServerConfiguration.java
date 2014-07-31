package com.github.signed.inmemory.jndi;

import com.github.signed.inmemory.AddressAndPort;

public class JndiServerConfiguration {
    public final AddressAndPort jndi;
    public final AddressAndPort rmi;

    public JndiServerConfiguration(AddressAndPort jndi, AddressAndPort rmi) {
        this.jndi = jndi;
        this.rmi = rmi;
    }
}

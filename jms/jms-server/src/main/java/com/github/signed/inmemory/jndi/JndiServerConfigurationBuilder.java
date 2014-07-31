package com.github.signed.inmemory.jndi;

import com.github.signed.inmemory.AddressAndPort;

public class JndiServerConfigurationBuilder {

    public static JndiServerConfigurationBuilder anyJndiServerConfigurationBut() {
        JndiServerConfigurationBuilder builder = new JndiServerConfigurationBuilder();
        builder.bindJndiTo(1099);
        builder.bindRmiTo(1098);
        return builder;
    }

    private AddressAndPort jndi;
    private AddressAndPort rmi;

    public JndiServerConfigurationBuilder bindRmiTo(int port) {
        rmi = new AddressAndPort("localhost", port);
        return this;
    }

    public JndiServerConfigurationBuilder bindJndiTo(int port) {
        jndi = new AddressAndPort("localhost", port);
        return this;
    }

    public JndiServerConfiguration build() {
        return new JndiServerConfiguration(jndi, rmi);
    }
}

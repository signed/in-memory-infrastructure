package com.github.signed.inmemory.jndi;

import com.github.signed.inmemory.AddressAndPort;

public class JndiServerConfigurationBuilder {

    public static JndiServerConfigurationBuilder anyJndiServerConfigurationBut() {
        JndiServerConfigurationBuilder builder = new JndiServerConfigurationBuilder();
        builder.bindJndiTo("localhost", 1099);
        builder.bindRmiTo("localhost", 1098);
        return builder;
    }

    private AddressAndPort jndi;
    private AddressAndPort rmi;

    public JndiServerConfigurationBuilder bindRmiTo(String bindAddressRmi, int port) {
        rmi = new AddressAndPort(bindAddressRmi, port);
        return this;
    }

    public JndiServerConfigurationBuilder bindJndiTo(String jndiBindAddress, int port) {
        jndi = new AddressAndPort(jndiBindAddress, port);
        return this;
    }

    public JndiServerConfiguration build() {
        return new JndiServerConfiguration(jndi, rmi);
    }
}

package com.github.signed.inmemory.jms;

public class JndiConfigurationBuilder {

    public static JndiConfigurationBuilder anyJndiServerConfigurationBut() {
        JndiConfigurationBuilder builder = new JndiConfigurationBuilder();
        builder.bindJndiTo("localhost", 1099);
        builder.bindRmiTo("localhost", 1098);
        return builder;
    }

    private AddressAndPort jndi;
    private AddressAndPort rmi;

    public JndiConfigurationBuilder bindRmiTo(String bindAddressRmi, int port) {
        rmi = new AddressAndPort(bindAddressRmi, port);
        return this;
    }

    public JndiConfigurationBuilder bindJndiTo(String jndiBindAddress, int port) {
        jndi = new AddressAndPort(jndiBindAddress, port);
        return this;
    }

    public JndiConfiguration build() {
        return new JndiConfiguration(jndi, rmi);
    }
}

package com.github.signed.inmemory.jms;

public class JndiConfigurationBuilder {

    public static JndiConfiguration anyJndiConfiguration() {
        return anyJndiConfigurationBut().build();
    }

    public static JndiConfigurationBuilder anyJndiConfigurationBut() {
        JndiConfigurationBuilder builder = new JndiConfigurationBuilder();
        builder.bindJndiTo("localhost", 1099);
        String bindAddressRmi = "localhost";
        int port = 1098;
        builder.bindRmiTo(bindAddressRmi, port);
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

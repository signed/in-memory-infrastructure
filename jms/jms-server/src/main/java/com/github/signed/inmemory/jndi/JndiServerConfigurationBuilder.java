package com.github.signed.inmemory.jndi;

import com.github.signed.inmemory.AddressAndPort;
import com.github.signed.inmemory.ExplicitPort;
import com.github.signed.inmemory.LetJavaSocketFindFreePort;

public class JndiServerConfigurationBuilder {

    public static JndiServerConfigurationBuilder anyJndiServerConfigurationBut() {
        return new JndiServerConfigurationBuilder();
    }

    private AddressAndPort jndi = new AddressAndPort(new LetJavaSocketFindFreePort());
    private AddressAndPort rmi = new AddressAndPort(new LetJavaSocketFindFreePort());

    public JndiServerConfigurationBuilder bindRmiTo(int port) {
        rmi = new AddressAndPort(new ExplicitPort(port));
        return this;
    }

    public JndiServerConfigurationBuilder bindJndiTo(int port) {
        jndi = new AddressAndPort(new ExplicitPort(port));
        return this;
    }

    public JndiServerConfiguration build() {
        return new JndiServerConfiguration(jndi, rmi);
    }
}

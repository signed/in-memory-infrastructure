package com.github.signed.inmemory.jndi;

import com.github.signed.inmemory.shared.configuration.AddressAndPort;
import com.github.signed.inmemory.shared.configuration.ExplicitPort;
import com.github.signed.inmemory.shared.configuration.LetJavaSocketFindFreePort;
import com.github.signed.inmemory.shared.configuration.Port;

public class JndiServerConfigurationBuilder {

    public static JndiServerConfigurationBuilder anyJndiServerConfigurationBut() {
        return new JndiServerConfigurationBuilder();
    }

    private AddressAndPort jndi = new AddressAndPort(new LetJavaSocketFindFreePort());
    private AddressAndPort rmi = new AddressAndPort(new LetJavaSocketFindFreePort());

    public JndiServerConfigurationBuilder listenOnPort(int port) {
        return listenOnPort(new ExplicitPort(port));
    }

    public JndiServerConfigurationBuilder listenOnPort(Port port) {
        jndi = new AddressAndPort(port);
        return this;
    }

    public JndiServerConfiguration build() {
        return new JndiServerConfiguration(jndi, rmi);
    }
}

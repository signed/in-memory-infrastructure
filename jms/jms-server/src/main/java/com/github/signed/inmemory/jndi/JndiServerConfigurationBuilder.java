package com.github.signed.inmemory.jndi;

import com.github.signed.inmemory.shared.configuration.AddressAndPort;
import com.github.signed.inmemory.shared.configuration.ExplicitPort;
import com.github.signed.inmemory.shared.configuration.LetJavaSocketFindFreePort;

public class JndiServerConfigurationBuilder {

    public static JndiServerConfigurationBuilder anyJndiServerConfigurationBut() {
        return new JndiServerConfigurationBuilder();
    }

    private AddressAndPort jndi = new AddressAndPort(new LetJavaSocketFindFreePort());
    private AddressAndPort rmi = new AddressAndPort(new LetJavaSocketFindFreePort());

    public JndiServerConfigurationBuilder bindJndiTo(int port) {
        jndi = new AddressAndPort(new ExplicitPort(port));
        return this;
    }

    public JndiServerConfiguration build() {
        return new JndiServerConfiguration(jndi, rmi);
    }
}

package com.github.signed.inmemory.jms;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.github.signed.inmemory.jndi.JndiServer;
import com.github.signed.inmemory.jndi.JndiServerConfiguration;
import com.github.signed.inmemory.jndi.JndiServerConfigurationBuilder;

public class JndiServer_Test {

    @Test
    public void byDefaultConfigureJndiServerToSelectAnOpenPort() throws Exception {
        JndiServerConfiguration configuration = JndiServerConfigurationBuilder.anyJndiServerConfigurationBut().build();
        JndiServer jndiServer = new JndiServer(configuration);
        jndiServer.configure();

        assertThat(jndiServer.providerUrl(), is("jnp://localhost:0"));
    }

    @Test
    public void allowTheUserToPickAnExplicitPort() throws Exception {
        JndiServerConfiguration configuration = JndiServerConfigurationBuilder.anyJndiServerConfigurationBut().bindJndiTo(1979).build();
        JndiServer jndiServer = new JndiServer(configuration);
        jndiServer.configure();

        assertThat(jndiServer.providerUrl(), is("jnp://localhost:1979"));
    }
}
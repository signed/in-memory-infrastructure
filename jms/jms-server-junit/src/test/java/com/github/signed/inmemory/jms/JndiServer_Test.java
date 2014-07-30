package com.github.signed.inmemory.jms;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.naming.Context;

import org.junit.Test;

public class JndiServer_Test {

    @Test
    public void constructTheProviderUrlOutOfTheConfiguration() throws Exception {
        JndiConfiguration configuration = JndiConfigurationBuilder.anyJndiServerConfigurationBut().bindJndiTo("127.0.0.1", 1979).build();
        JndiServer jndiServer = new JndiServer(configuration);

        assertThat(jndiServer.createContext().getEnvironment().get(Context.PROVIDER_URL).toString(), containsString("127.0.0.1:1979"));
    }

    @Test
    public void provideAccessToTheProviderUrlRequiredToConnectToTheServer() throws Exception {
        JndiConfiguration configuration = JndiConfigurationBuilder.anyJndiServerConfigurationBut().bindJndiTo("127.0.0.1", 1979).build();
        JndiServer jndiServer = new JndiServer(configuration);

        assertThat(jndiServer.providerUrl(), is("jnp://127.0.0.1:1979"));
    }
}
package com.github.signed.inmemory.jms;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class JmsServer_Test {

    @Test
    public void returnConnectionFactoryNameFromConfiguration() throws Exception {
        JmsServerConfiguration configuration = new JmsServerConfigurationBuilder().build();
        JmsServer jmsServer = new JmsServer(configuration);

        assertThat(jmsServer.connectionFactoryName(), is(configuration.connectionFactoryName()));
    }
}
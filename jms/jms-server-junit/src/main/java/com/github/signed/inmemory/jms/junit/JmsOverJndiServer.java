package com.github.signed.inmemory.jms.junit;

import org.junit.rules.ExternalResource;

import com.github.signed.inmemory.jms.JmsServer;
import com.github.signed.inmemory.jms.JmsServerConfiguration;
import com.github.signed.inmemory.jms.JndiConfiguration;
import com.github.signed.inmemory.jms.JndiServer;

public class JmsOverJndiServer extends ExternalResource {
    private final JndiServer jndiServer;
    private final JmsServer jmsServer;

    public JmsOverJndiServer(JndiConfiguration jndiServerConfiguration, JmsServerConfiguration jmsServerConfiguration) {
        jndiServer = new JndiServer(jndiServerConfiguration);
        jmsServer = new JmsServer(jmsServerConfiguration);
    }

    @Override
    protected void before() throws Throwable {
        startJndi();
        startJms();
    }

    private void startJndi() {
        jndiServer.configure();
        jndiServer.start();
    }

    private void startJms() {
        try {
            jmsServer.configure();
            jmsServer.attachQueuesAndTopicsTo(jndiServer.createContext());
            jmsServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void after() {
        jmsServer.stop();
        jndiServer.stop();
    }

    public String connectionFactoryName() {
        return jmsServer.connectionFactoryName();
    }

    public String providerUrl() {
        return jndiServer.providerUrl();
    }

    public String initialContextFactory() {
        return jndiServer.initialContextFactoryFullQualifiedClassName();
    }
}
package com.github.signed.inmemory.jms.junit;

import org.junit.rules.ExternalResource;

import com.github.signed.inmemory.jms.JmsServer;
import com.github.signed.inmemory.jms.JmsServerConfiguration;
import com.github.signed.inmemory.jndi.JndiServerConfiguration;
import com.github.signed.inmemory.jndi.JndiServer;

public class JmsOverJndiServer extends ExternalResource {
    private final JndiServer jndiServer;
    private final JmsServer jmsServer;

    public JmsOverJndiServer(JndiServerConfiguration jndiServerConfiguration, JmsServerConfiguration jmsServerConfiguration) {
        jndiServer = new JndiServer(jndiServerConfiguration);
        jmsServer = new JmsServer(jmsServerConfiguration);
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

    public void start() {
        startJndi();
        startJms();
    }

    public void stop() {
        jndiServer.configure();
        jndiServer.start();
    }

    @Override
    protected void before() throws Throwable {
        start();
    }

    private void startJndi() {
        stop();
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
}
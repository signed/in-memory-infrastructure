package com.github.signed.inmemory.jms.junit;

import java.net.UnknownHostException;

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

    private void startJndi() throws UnknownHostException {
        jndiServer.configure();
        jndiServer.start();
    }

    private void startJms() throws Exception {
        jmsServer.configure();
        jmsServer.attachQueuesAndTopicsTo(jndiServer.createContext());
        jmsServer.start();
    }

    @Override
    protected void after() {
        jmsServer.stop();
        jndiServer.stop();
    }
}
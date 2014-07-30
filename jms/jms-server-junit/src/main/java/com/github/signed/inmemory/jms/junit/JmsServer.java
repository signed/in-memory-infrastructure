package com.github.signed.inmemory.jms.junit;

import javax.naming.Context;

import org.junit.rules.ExternalResource;

public class JmsServer extends ExternalResource {


    public int port;

    private JndiServer jndiServer;
    private com.github.signed.inmemory.jms.JmsServer jmsServer;


    @Override
    protected void before() throws Throwable {
        jndiServer = new JndiServer();
        jndiServer.configure();
        jndiServer.start();

        Context context = jndiServer.createContext();

        jmsServer = new com.github.signed.inmemory.jms.JmsServer();
        jmsServer.configure();
        jmsServer.attachQueuesAndTopicsTo(context);
        jmsServer.start();
    }

    @Override
    protected void after() {
        try {
            jmsServer.stop();
            jndiServer.stop();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}

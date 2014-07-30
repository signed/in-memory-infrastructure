package com.github.signed.inmemory.jms.junit;

import javax.naming.Context;

import org.junit.rules.ExternalResource;

import com.github.signed.inmemory.jms.JmsServer;
import com.github.signed.inmemory.jms.JndiConfigurationBuilder;
import com.github.signed.inmemory.jms.JndiServer;

public class JmsOverJndiServer extends ExternalResource {


    private JndiServer jndiServer;
    private JmsServer jmsServer;


    @Override
    protected void before() throws Throwable {
        jndiServer = new JndiServer(JndiConfigurationBuilder.anyJndiConfiguration());
        jndiServer.configure();
        jndiServer.start();

        Context context = jndiServer.createContext();

        jmsServer = new JmsServer();
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

package com.github.signed.inmemory.jms;

import java.net.UnknownHostException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jnp.server.Main;
import org.jnp.server.NamingBeanImpl;

public class JndiServer {

    private static final String InitialContextFactory = "org.jnp.interfaces.NamingContextFactory";

    private final Main jndiServer = new Main();
    private final NamingBeanImpl naming = new NamingBeanImpl();
    private final JndiConfiguration configuration;

    public JndiServer(JndiConfiguration configuration) {
        this.configuration = configuration;
    }

    public void configure() throws UnknownHostException {
        jndiServer.setNamingInfo(naming);
        jndiServer.setBindAddress(configuration.jndi.address);
        jndiServer.setPort(configuration.jndi.port);
        jndiServer.setRmiBindAddress(configuration.rmi.address);
        jndiServer.setRmiPort(configuration.rmi.port);
    }

    public Context createContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactory);
        env.put(Context.PROVIDER_URL, providerUrl());
        return new InitialContext(env);
    }

    public String initialContextFactoryFullQualifiedClassName(){
        return InitialContextFactory;
    }

    public String providerUrl() {
        return String.format("jnp://%s:%d", configuration.jndi.address, configuration.jndi.port);
    }

    public void start() throws Exception {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactory);
        naming.start();
        jndiServer.start();
    }

    public void stop() {
        jndiServer.stop();
        naming.stop();
    }
}

package com.github.signed.inmemory.jndi;

import java.net.UnknownHostException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jnp.server.Main;
import org.jnp.server.NamingBeanImpl;

public class JndiServer {

    private static final String InitialContextFactory = "org.jnp.interfaces.NamingContextFactory";
    public static final int FindAnyOpenPort = 0;

    private final Main jndiServer = new Main();
    private final NamingBeanImpl naming = new NamingBeanImpl();
    private final JndiServerConfiguration configuration;

    public JndiServer(JndiServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public void configure()  {
        try {
            jndiServer.setNamingInfo(naming);
            jndiServer.setBindAddress(configuration.jndi.address());
            jndiServer.setPort(FindAnyOpenPort);
            jndiServer.setRmiBindAddress(configuration.rmi.address());
            jndiServer.setRmiPort(FindAnyOpenPort);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
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
        return String.format("jnp://%s:%d", configuration.jndi.address(), port());
    }

    private int port() {
        return jndiServer.getPort();
    }

    public void start() {
        try {
            //TODO unset system property after server is started, not sure this will work
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactory);
            naming.start();
            jndiServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        jndiServer.stop();
        naming.stop();
    }
}

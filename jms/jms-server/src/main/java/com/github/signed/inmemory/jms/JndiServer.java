package com.github.signed.inmemory.jms;

import java.net.UnknownHostException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jnp.server.Main;
import org.jnp.server.NamingBeanImpl;

public class JndiServer {

    private final Main jndiServer = new Main();
    private final NamingBeanImpl naming = new NamingBeanImpl();

    public void configure() throws UnknownHostException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        jndiServer.setNamingInfo(naming);
        jndiServer.setPort(1099);
        jndiServer.setBindAddress("localhost");
        jndiServer.setRmiPort(1098);
        jndiServer.setRmiBindAddress("localhost");
    }

    public void start() throws Exception {
        naming.start();
        jndiServer.start();
    }

    public void stop() {
        jndiServer.stop();
        naming.stop();
    }

    public Context createContext() throws NamingException {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        return new InitialContext(env);
    }
}

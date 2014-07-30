package com.github.signed.inmemory.jms.junit;

import java.net.UnknownHostException;

import org.jnp.server.Main;
import org.jnp.server.NamingBeanImpl;

public class JndiServer {

    private final Main jndiServer = new Main();
    private final NamingBeanImpl naming = new NamingBeanImpl();

    public void start() throws Exception {
        naming.start();
        jndiServer.start();
    }

    public void stop() {
        jndiServer.stop();
        naming.stop();
    }

    public void configure() throws UnknownHostException {
        jndiServer.setNamingInfo(naming);
        jndiServer.setPort(1099);
        jndiServer.setBindAddress("localhost");
        jndiServer.setRmiPort(1098);
        jndiServer.setRmiBindAddress("localhost");
    }
}

package com.github.signed.inmemory.jms;

import java.net.ServerSocket;

import org.junit.Test;

public class JmsServer_TryDifferentPortIfConfiguredOneIsInUseTest {

    @Test
    public void lookForANewPortIfOriginalPortIsNotInUse() throws Exception {
        ServerSocket serverSocket = new ServerSocket(1979);

        JmsServer jmsServer = new JmsServer(JmsServerConfigurationBuilder.anyJmsServerConfigurationBut().bindTo("localhost", 1979).build());
        jmsServer.configure();
        jmsServer.start();

        jmsServer.stop();


        serverSocket.close();
    }
}
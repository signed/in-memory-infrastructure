package com.github.signed.inmemory.jms.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.JMSConfiguration;
import org.hornetq.jms.server.config.JMSQueueConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.hornetq.jms.server.embedded.EmbeddedJMS;
import org.junit.rules.ExternalResource;

public class JmsServer extends ExternalResource {


    public int port;
    private EmbeddedJMS jmsServer;


    @Override
    protected void before() throws Throwable {
        // Step 1. Create HornetQ core configuration, and set the properties accordingly
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setJournalDirectory("build/data/journal");
        configuration.setSecurityEnabled(false);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(TransportConstants.HOST_PROP_NAME, "localhost");
        params.put(TransportConstants.PORT_PROP_NAME, 5446);
        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName(), params));
        TransportConfiguration connectorConfig = new TransportConfiguration(NettyConnectorFactory.class.getName());
        configuration.getConnectorConfigurations().put("connector", connectorConfig);

        // Step 2. Create the JMS configuration
        JMSConfiguration jmsConfig = new JMSConfigurationImpl();

        // Step 3. Configure the JMS ConnectionFactory
        ArrayList<String> connectorNames = new ArrayList<String>();
        connectorNames.add("connector");
        ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl("cf", false, connectorNames, "/cf");
        jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

        // Step 4. Configure the JMS Queue
        JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl("queue1", null, false, "/queue/queue1");
        jmsConfig.getQueueConfigurations().add(queueConfig);

        // Step 5. Start the JMS Server using the HornetQ core server and the JMS configuration
        jmsServer = new EmbeddedJMS();
        jmsServer.setConfiguration(configuration);
        jmsServer.setJmsConfiguration(jmsConfig);
        jmsServer.start();
    }

    @Override
    protected void after() {
        try{
            jmsServer.stop();
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

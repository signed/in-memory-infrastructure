package com.github.signed.inmemory.jms.hornetq;

import java.util.ArrayList;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.JMSConfiguration;
import org.hornetq.jms.server.config.JMSQueueConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.hornetq.jms.server.embedded.EmbeddedJMS;

public class HornetQServer {

    public static void main(String[] args) throws Exception {
        new HornetQServer().start();
    }


    public void start() throws Exception {
        // Step 5. Start the JMS Server using the HornetQ core server and the JMS configuration
        EmbeddedJMS jmsServer = new EmbeddedJMS();
        jmsServer.setConfiguration(hornetQCoreConfiguration());
        jmsServer.setJmsConfiguration(jmsConfiguration());
        jmsServer.start();
        System.out.println("Started Embedded JMS Server");
    }

    private Configuration hornetQCoreConfiguration() {
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setJournalDirectory("build/data/journal");
        configuration.setSecurityEnabled(false);
        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName()));
        configuration.getConnectorConfigurations().put("connector", new TransportConfiguration(NettyConnectorFactory.class.getName()));
        return configuration;
    }

    private JMSConfiguration jmsConfiguration() {
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
        return jmsConfig;
    }
}

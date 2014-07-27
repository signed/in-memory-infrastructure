package com.github.signed.inmemory.jms.hornetq;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

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
        // Step 1. Create HornetQ core configuration, and set the properties accordingly
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setJournalDirectory("target/data/journal");
        configuration.setSecurityEnabled(false);
        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName()));
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
        EmbeddedJMS jmsServer = new EmbeddedJMS();
        jmsServer.setConfiguration(configuration);
        jmsServer.setJmsConfiguration(jmsConfig);
        jmsServer.start();
        System.out.println("Started Embedded JMS Server");

        // Step 6. Lookup JMS resources defined in the configuration
        ConnectionFactory cf = (ConnectionFactory) jmsServer.lookup("/cf");
        Queue queue = (Queue) jmsServer.lookup("/queue/queue1");

        // Step 7. Send and receive a message using JMS API
        Connection connection = null;
        try {
            connection = cf.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage("Hello sent at " + new Date());
            System.out.println("Sending message: " + message.getText());
            producer.send(message);
            MessageConsumer messageConsumer = session.createConsumer(queue);
            connection.start();
            TextMessage messageReceived = (TextMessage) messageConsumer.receive(1000);
            System.out.println("Received message:" + messageReceived.getText());
        } finally {
            if (connection != null) {
                connection.close();
            }

            // Step 11. Stop the JMS server
            jmsServer.stop();
            System.out.println("Stopped the JMS Server");
        }
    }

    public void stackoverflow() throws Exception {
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setSecurityEnabled(false);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("host", "localhost");
        map.put("port", 5445);
        TransportConfiguration acceptorTransportConfiguration = new TransportConfiguration(NettyAcceptorFactory.class.getName(), map);
        configuration.getAcceptorConfigurations().add(acceptorTransportConfiguration);

        TransportConfiguration connectorTransportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName());
        configuration.getConnectorConfigurations().put("connector", connectorTransportConfiguration);


        // Step 2. Create the JMS configuration
        JMSConfiguration jmsConfig = new JMSConfigurationImpl();

        // Step 3. Configure the JMS ConnectionFactory
        ArrayList<String> connectorNames = new ArrayList<String>();
        connectorNames.add("connector");
        ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl("ConnectionFactory", false, connectorNames, "/ConnectionFactory");
        jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

        // Step 4. Configure the JMS Queue
        JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl("exampleQueue", null, false, "/queue/exampleQueue");
        jmsConfig.getQueueConfigurations().add(queueConfig);

        // Step 5. Start the JMS Server using the HornetQ core server and the JMS configuration
        EmbeddedJMS jmsServer = new EmbeddedJMS();
        jmsServer.setConfiguration(configuration);
        jmsServer.setJmsConfiguration(jmsConfig);

        jmsServer.start();
        System.out.println("Started Embedded JMS Server");
    }
}

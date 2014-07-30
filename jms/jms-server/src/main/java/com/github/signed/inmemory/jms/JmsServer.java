package com.github.signed.inmemory.jms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.hornetq.jms.server.config.ConnectionFactoryConfiguration;
import org.hornetq.jms.server.config.JMSConfiguration;
import org.hornetq.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSConfigurationImpl;
import org.hornetq.jms.server.config.impl.JMSQueueConfigurationImpl;
import org.hornetq.jms.server.config.impl.TopicConfigurationImpl;
import org.hornetq.jms.server.embedded.EmbeddedJMS;

public class JmsServer {
    private final EmbeddedJMS embeddedJms = new EmbeddedJMS();
    private final JmsServerConfiguration configuration;

    public JmsServer(JmsServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public void configure() {
        embeddedJms.setConfiguration(coreConfiguration(configuration.host));
        embeddedJms.setJmsConfiguration(jmsConfiguration());
    }

    public void attachQueuesAndTopicsTo(Context context) {
        embeddedJms.setContext(context);
    }

    public void start() throws Exception {
        embeddedJms.start();
    }

    public void stop() {
        try {
            embeddedJms.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JMSConfiguration jmsConfiguration() {
        JMSConfiguration jmsConfig = new JMSConfigurationImpl();
        configureConnectionFactory(jmsConfig);
        configureQueues(jmsConfig);
        configureTopics(jmsConfig);
        return jmsConfig;
    }

    private void configureConnectionFactory(JMSConfiguration jmsConfig) {
        ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl(configuration.connectionFactoryName(), false, connectorNames(), configuration.connectionFactoryName());
        jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);
    }

    private void configureTopics(JMSConfiguration jmsConfig) {
        Iterable<TopicConfiguration> topicConfigurations = configuration.topics();
        for (TopicConfiguration configuration : topicConfigurations) {
            jmsConfig.getTopicConfigurations().add(new TopicConfigurationImpl(configuration.name(), configuration.name()));
        }
    }

    private void configureQueues(JMSConfiguration jmsConfig) {
        List<QueueConfiguration> queueConfigurations = configuration.queues();
        for (QueueConfiguration configuration : queueConfigurations) {
            jmsConfig.getQueueConfigurations().add(new JMSQueueConfigurationImpl(configuration.name(), null, false, configuration.name()));
        }
    }

    private ArrayList<String> connectorNames() {
        ArrayList<String> connectorNames = new ArrayList<String>();
        connectorNames.add("connector");
        return connectorNames;
    }

    private Configuration coreConfiguration(AddressAndPort host) {
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setJournalDirectory("build/data/journal");
        configuration.setSecurityEnabled(false);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(TransportConstants.HOST_PROP_NAME, host.address);
        params.put(TransportConstants.PORT_PROP_NAME, host.port);
        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName(), params));
        TransportConfiguration connectorConfig = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
        configuration.getConnectorConfigurations().put("connector", connectorConfig);
        return configuration;
    }
}

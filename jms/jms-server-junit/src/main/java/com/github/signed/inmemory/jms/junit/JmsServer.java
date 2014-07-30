package com.github.signed.inmemory.jms.junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;

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
import org.hornetq.jms.server.config.impl.TopicConfigurationImpl;
import org.hornetq.jms.server.embedded.EmbeddedJMS;
import org.junit.rules.ExternalResource;

public class JmsServer extends ExternalResource {


    public int port;
    private EmbeddedJMS jmsServer;

    private JndiServer jndiServer;

    public JmsServer() {
    }


    @Override
    protected void before() throws Throwable {
        jndiServer = new JndiServer();
        jndiServer.configure();
        jndiServer.start();

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        Context context = new InitialContext(env);

        jmsServer = new EmbeddedJMS();
        jmsServer.setConfiguration(coreConfiguration());
        JMSConfiguration jmsConfiguration = jmsConfiguration();
        jmsServer.setJmsConfiguration(jmsConfiguration);
        jmsServer.setContext(context);
        jmsServer.start();
    }

    private JMSConfiguration jmsConfiguration() {
        JMSConfiguration jmsConfig = new JMSConfigurationImpl();
        ArrayList<String> connectorNames = new ArrayList<String>();
        connectorNames.add("connector");
        ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl("cf", false, connectorNames, "/cf");
        jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

        JMSQueueConfiguration queueConfig = new JMSQueueConfigurationImpl("queue1", null, false, "/queue/queue1");
        jmsConfig.getQueueConfigurations().add(queueConfig);

        jmsConfig.getTopicConfigurations().add(new TopicConfigurationImpl("topic1", "topic/topic1"));
        return jmsConfig;
    }

    private Configuration coreConfiguration() {
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setJournalDirectory("build/data/journal");
        configuration.setSecurityEnabled(false);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(TransportConstants.HOST_PROP_NAME, "localhost");
        params.put(TransportConstants.PORT_PROP_NAME, 5446);
        configuration.getAcceptorConfigurations().add(new TransportConfiguration(NettyAcceptorFactory.class.getName(), params));
        TransportConfiguration connectorConfig = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);
        configuration.getConnectorConfigurations().put("connector", connectorConfig);
        return configuration;
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

package com.github.signed.inmemory.jms.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.remoting.impl.netty.TransportConstants;
import org.junit.Rule;
import org.junit.Test;

public class JmsServer_ConnectTest {


    @Rule
    public JmsServer jmsServer = new JmsServer();

    @Test
    public void sendAndReceiveMessageDirectlyWithHornetQ() throws Exception {
        Map<String, Object> connectionParams = new HashMap<String, Object>();
        connectionParams.put(TransportConstants.HOST_PROP_NAME, "localhost");
        connectionParams.put(TransportConstants.PORT_PROP_NAME, 5446);
        ConnectionFactory cf = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, new TransportConfiguration(NettyConnectorFactory.class.getName(), connectionParams));
        Queue queue = HornetQJMSClient.createQueue("queue1");

        assertThatProduceConsumeRoundTripIsWorking(cf, queue);
    }

    @Test
    public void testStackoverflowClient() throws Exception {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
        Context context = new InitialContext(env);
        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("/cf");
        Queue queue = (Queue) context.lookup("queue/queue1");

        assertThatProduceConsumeRoundTripIsWorking(connectionFactory, queue);
    }

    private void assertThatProduceConsumeRoundTripIsWorking(ConnectionFactory connectionFactory, Queue queue) throws JMSException {
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("Hello World");
        producer.send(message);
        MessageConsumer messageConsumer = session.createConsumer(queue);
        connection.start();
        TextMessage messageReceived = (TextMessage) messageConsumer.receive(1000);

        assertThat(messageReceived.getText(), is("Hello World"));
        connection.close();
    }
}
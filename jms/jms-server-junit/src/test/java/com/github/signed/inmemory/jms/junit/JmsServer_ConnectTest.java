package com.github.signed.inmemory.jms.junit;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.print.attribute.standard.Destination;

import org.junit.Rule;
import org.junit.Test;

import com.oracle.jrockit.jfr.Producer;

public class JmsServer_ConnectTest {


    @Rule
    public JmsServer jmsServer = new JmsServer();

    @Test
    public void startJmsServerSoClientsCanOpenAnInitialContext() throws Exception {
        Properties props = new Properties();
        props.put("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
        props.put("java.naming.provider.url", "jnp://localhost:1099");
        props.put("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
        InitialContext initialContext = new InitialContext(props);
    }

    @Test
    public void stackoverflow() throws Exception {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");

        Context context = new InitialContext(env);

        ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Destination destination = (Destination) context.lookup("/queue/exampleQueue");

        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Producer producer = session.createProducer(destination);
        connection.start();

        TextMessage message = session.createTextMessage("Hello sent at " + new Date());
        System.out.println("Sending message: " + message.getText());
        producer.send(message);


    }
}
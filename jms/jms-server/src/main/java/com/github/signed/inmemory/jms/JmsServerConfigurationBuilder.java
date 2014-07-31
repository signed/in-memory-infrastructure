package com.github.signed.inmemory.jms;

import java.util.ArrayList;
import java.util.List;

import com.github.signed.inmemory.AddressAndPort;
import com.github.signed.inmemory.ExplicitPort;
import com.github.signed.inmemory.RandomUserPort;

public class JmsServerConfigurationBuilder {

    public static JmsServerConfigurationBuilder anyJmsServerConfigurationBut() {
        return new JmsServerConfigurationBuilder();
    }

    private final List<TopicConfiguration> topicsToCreate = new ArrayList<TopicConfiguration>();

    private final List<QueueConfiguration> queuesToCreate = new ArrayList<QueueConfiguration>();
    private AddressAndPort host = new AddressAndPort(new RandomUserPort());

    public JmsServerConfigurationBuilder bindTo(int port) {
        host = new AddressAndPort(new ExplicitPort(port));
        return this;
    }

    public JmsServerConfigurationBuilder createQueue(String name) {
        queuesToCreate.add(new QueueConfiguration(name));
        return this;
    }

    public JmsServerConfigurationBuilder createTopic(String name) {
        topicsToCreate.add(new TopicConfiguration(name));
        return this;
    }

    public JmsServerConfiguration build() {
        return new JmsServerConfiguration(host, topicsToCreate, queuesToCreate);
    }

}

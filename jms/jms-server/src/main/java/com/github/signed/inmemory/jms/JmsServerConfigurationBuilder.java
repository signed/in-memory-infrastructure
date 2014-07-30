package com.github.signed.inmemory.jms;

import java.util.ArrayList;
import java.util.List;

public class JmsServerConfigurationBuilder {

    public static JmsServerConfigurationBuilder anyJmsServerConfigurationBut() {
        JmsServerConfigurationBuilder builder = new JmsServerConfigurationBuilder();
        builder.bindTo("localhost", 5446);
        return builder;
    }

    private final List<TopicConfiguration> topicsToCreate = new ArrayList<TopicConfiguration>();
    private final List<QueueConfiguration> queuesToCreate = new ArrayList<QueueConfiguration>();
    private AddressAndPort host;

    public JmsServerConfigurationBuilder bindTo(String address, int port) {
        host = new AddressAndPort(address, port);
        return this;
    }

    public JmsServerConfiguration build() {
        return new JmsServerConfiguration(host, "cf", topicsToCreate, queuesToCreate);
    }

    public JmsServerConfigurationBuilder createQueue(String name) {
        queuesToCreate.add(new QueueConfiguration(name));
        return this;
    }

    public JmsServerConfigurationBuilder createTopic(String name) {
        topicsToCreate.add(new TopicConfiguration(name));
        return this;
    }
}

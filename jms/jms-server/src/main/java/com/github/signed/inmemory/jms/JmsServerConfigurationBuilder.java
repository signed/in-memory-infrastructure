package com.github.signed.inmemory.jms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.signed.inmemory.AddressAndPort;

public class JmsServerConfigurationBuilder {

    public static JmsServerConfigurationBuilder anyJmsServerConfigurationBut() {
        JmsServerConfigurationBuilder builder = new JmsServerConfigurationBuilder();
        builder.bindTo(5446);
        return builder;
    }

    private final List<TopicConfiguration> topicsToCreate = new ArrayList<TopicConfiguration>();

    private final List<QueueConfiguration> queuesToCreate = new ArrayList<QueueConfiguration>();
    private AddressAndPort host;

    public JmsServerConfigurationBuilder bindTo(int port) {
        int offset = new Random(0).nextInt(10000);
        host = new AddressAndPort("localhost", port+offset);
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

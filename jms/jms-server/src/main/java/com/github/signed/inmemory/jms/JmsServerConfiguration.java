package com.github.signed.inmemory.jms;

import com.github.signed.inmemory.AddressAndPort;

public class JmsServerConfiguration {
    private final AddressAndPort host;
    private final Iterable<TopicConfiguration> topicConfigurations;
    private final Iterable<QueueConfiguration> queuesToCreate;
    private final String connectionFactoryName;

    public JmsServerConfiguration(AddressAndPort host, String connectionFactoryName, Iterable<TopicConfiguration> topicConfigurations, Iterable<QueueConfiguration> queuesToCreate) {
        this.host = host;
        this.topicConfigurations = topicConfigurations;
        this.queuesToCreate = queuesToCreate;
        this.connectionFactoryName = connectionFactoryName;
    }

    public Iterable<QueueConfiguration> queues() {
        return queuesToCreate;
    }

    public Iterable<TopicConfiguration> topics() {
        return topicConfigurations;
    }

    public String connectionFactoryName() {
        return connectionFactoryName;
    }

    public AddressAndPort host(){
        return host;
    }
}

package com.github.signed.inmemory.jms;

import com.github.signed.inmemory.shared.configuration.AddressAndPort;

public class JmsServerConfiguration {
    private final AddressAndPort host;
    private final Iterable<TopicConfiguration> topicConfigurations;
    private final Iterable<QueueConfiguration> queuesToCreate;

    public JmsServerConfiguration(AddressAndPort host, Iterable<TopicConfiguration> topicConfigurations, Iterable<QueueConfiguration> queuesToCreate) {
        this.host = host;
        this.topicConfigurations = topicConfigurations;
        this.queuesToCreate = queuesToCreate;
    }

    public Iterable<QueueConfiguration> queues() {
        return queuesToCreate;
    }

    public Iterable<TopicConfiguration> topics() {
        return topicConfigurations;
    }

    public String connectionFactoryName() {
        return "ConnectionFactoryName";
    }

    public AddressAndPort host(){
        return host;
    }
}

package com.github.signed.inmemory.jms;

import java.util.Collections;
import java.util.List;

public class JmsServerConfiguration {
    public final AddressAndPort host;

    public JmsServerConfiguration(AddressAndPort host) {
        this.host = host;
    }

    public List<QueueConfiguration> queues() {
        QueueConfiguration queueConfiguration = new QueueConfiguration("queue1");
        return Collections.singletonList(queueConfiguration);
    }

    public Iterable<TopicConfiguration> topics() {
        TopicConfiguration topicConfiguration = new TopicConfiguration("topic1");
        return Collections.singletonList(topicConfiguration);
    }

    public String connectionFactoryName() {
        return "cf";
    }
}

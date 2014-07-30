package com.github.signed.inmemory.jms;

public class TopicConfiguration {

    private final String name;

    public TopicConfiguration(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}

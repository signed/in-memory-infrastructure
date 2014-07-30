package com.github.signed.inmemory.jms;

public class QueueConfiguration {

    private final String name;

    public QueueConfiguration(String name) {
        this.name = name;
    }

    public String name(){
        return name;
    }
}

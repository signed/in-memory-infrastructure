package com.github.signed.inmemory.shared.configuration;

import java.util.Random;

/**
 * UserPort  range: 1024-49151
 * http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xhtml
 */
public class RandomUserPort implements Port {

    private final int port;

    public RandomUserPort() {
        int from = 1024;
        int to = 49151;
        port = from + new Random().nextInt(to - from + 1);
    }

    @Override
    public int port() {
        return port;
    }
}

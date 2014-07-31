package com.github.signed.inmemory;

import java.util.Random;

/**
 * UserPort  range: 1024-49151
 * http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xhtml
 */
public class RandomUserPort implements Port {

    private static final Random random = new Random(341882349);

    private final int port;

    public RandomUserPort() {
        int from = 1024;
        int to = 49151;
        port = from + random.nextInt(to - from + 1);
    }

    @Override
    public int port() {
        return port;
    }
}

package com.github.signed.inmemory.shared.configuration;

import java.util.Random;

/**
 * UserPort  range: 1024-49151
 * http://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xhtml
 */
public class RandomPort implements Port {

    public static final int HighestUserPort = 49151;

    public static Port AnyWithin(int from, int to){
        int port = randomPortWithin(from, to);
        return CreatePort(port);
    }

    public static Port AnyUserPort() {
        return CreatePort(randomPortWithin(1024, HighestUserPort));
    }

    private static Port CreatePort(int port) {
        return new RandomPort(port);
    }

    private static int randomPortWithin(int from, int to) {
        return from + new Random().nextInt(to - from + 1);
    }

    private final int port;

    private RandomPort(int port) {
        this.port = port;
    }

    @Override
    public int port() {
        return port;
    }
}

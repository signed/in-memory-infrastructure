package com.github.signed.inmemory.shared.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class RandomPortTest {

    @Test
    public void returnTheSameRandomPortOverAndOverAgain() throws Exception {
        Port randomPort = RandomPort.AnyUserPort();

        assertThat(randomPort.port(), is(randomPort.port()));
    }

    @Test
    public void letsHopeThatTheOddsAreWithUse() throws Exception {
        assertThat(RandomPort.AnyUserPort().port(), not(RandomPort.AnyUserPort().port()));
    }

    @Test
    public void pickAPortWithinRange() throws Exception {
        Port port = RandomPort.AnyWithin(12, 12);

        assertThat(port.port(), is(12));
    }
}
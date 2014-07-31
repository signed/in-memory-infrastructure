package com.github.signed.inmemory.shared.configuration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class RandomUserPortTest {

    @Test
    public void returnTheSameRandomPortOverAndOverAgain() throws Exception {
        RandomUserPort randomUserPort = new RandomUserPort();

        assertThat(randomUserPort.port(), is(randomUserPort.port()));
    }

    @Test
    public void letsHopeThatTheOddsAreWithUse() throws Exception {
        assertThat(new RandomUserPort().port(), not(new RandomUserPort().port()));
    }
}
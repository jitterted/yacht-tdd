package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class HandOfDiceEmptyTest {

    @Test
    public void nonEmptyDiceRollIsNotEmpty() throws Exception {
        HandOfDice handOfDice = HandOfDice.of(1, 2, 3, 4, 5);

        assertThat(handOfDice.isEmpty())
                .isFalse();
    }

    @Test
    public void emptyDiceRollIsEmpty() throws Exception {
        HandOfDice handOfDice = HandOfDice.unassigned();

        assertThat(handOfDice.isEmpty())
                .isTrue();
    }

}
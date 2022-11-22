package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DiceRollEmptyTest {

    @Test
    public void nonEmptyDiceRollIsNotEmpty() throws Exception {
        DiceRoll diceRoll = DiceRoll.of(1, 2, 3, 4, 5);

        assertThat(diceRoll.isEmpty())
                .isFalse();
    }

    @Test
    public void emptyDiceRollIsEmpty() throws Exception {
        DiceRoll diceRoll = DiceRoll.empty();

        assertThat(diceRoll.isEmpty())
                .isTrue();
    }

}
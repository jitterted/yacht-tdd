package com.jitterted.yacht.adapter.out.dieroller;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class DieRollerTest {

    @Test
    void rollerRollsMultipleDice() {
        DieRoller roller = DieRoller.createNull(5, 4, 3);

        // NEXT: Remove DiceRoller and distribute its work to Domain and Infra
        // and then eliminate roll()
        List<Integer> dice = roller.rollMultiple(3);

        assertThat(dice)
                .containsExactly(5, 4, 3);
    }

    @Test
    void nullRollerReturnsInfiniteOneValuesWhenNotConfigured() throws Exception {
        DieRoller roller = DieRoller.createNull();

        assertThat(roller.roll())
                .isEqualTo(1);
        assertThat(roller.roll())
                .isEqualTo(1);
        assertThat(roller.roll())
                .isEqualTo(1);
    }

    @Test
    void nullRollerCanBeConfiguredToReturnSpecificValues() throws Exception {
        DieRoller roller = DieRoller.createNull(6, 3, 4, 3);

        assertThat(roller.roll())
                .isEqualTo(6);
        assertThat(roller.roll())
                .isEqualTo(3);
        assertThat(roller.roll())
                .isEqualTo(4);
        assertThat(roller.roll())
                .isEqualTo(3);
    }

    @Test
    void nullRollerThrowsExceptionWhenRunsOutOfConfiguredRolls() throws Exception {
        DieRoller roller = DieRoller.createNull(2);
        roller.roll();

        assertThatThrownBy(() -> {
            roller.roll();
        }).isInstanceOf(NoSuchElementException.class)
          .hasMessage("No more rolls configured in Null DieRoller.");
    }

}
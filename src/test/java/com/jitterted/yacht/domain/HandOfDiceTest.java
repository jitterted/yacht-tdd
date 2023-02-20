package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class HandOfDiceTest {

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


    // ----- EDGE CASES -----


    @Test
    void fromConvertsEmptyHand() {
        assertThat(HandOfDice.from(Collections.emptyList()))
            .isEqualTo(HandOfDice.unassigned());
    }

    @Test
    void fromConvertsFullHand() {
        assertThat(HandOfDice.from(List.of(1, 2, 3, 4, 5)))
                .isEqualTo(HandOfDice.of(1, 2, 3, 4, 5));
    }

    @Test
    void fromThrowsExceptionWhenCurrentHandHasWrongNumberOfDice() {
        assertFromThrows(List.of(6, 6, 6));
    }

    @Test
    void fromThrowsExceptionWhenCurrentHandHasDiceThatAreTooLarge() {
        assertFromThrows(List.of(6, 5, 6, 7, 6));
    }

    @Test
    void fromThrowsExceptionWhenCurrentHandHasDiceThatAreTooSmall() {
        assertFromThrows(List.of(1, 2, 1, 0, 1));
    }

    @Test
    void fromThrowsRuntimeExceptionByDefault() {
        assertThatThrownBy(() -> HandOfDice.from(List.of(1, 2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid HandOfDice, dieRolls were: [1, 2]");
    }

    @Test
    void fromAllowsDefaultValueToBeSpecifiedForUseWithInvalidHands() {
        assertThat(HandOfDice.from(List.of(1, 2), HandOfDice::unassigned))
                .isEqualTo(HandOfDice.unassigned());
    }

    private void assertFromThrows(List<Integer> list) {
        assertThatThrownBy(() -> HandOfDice.from(list))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.domain.HandOfDice;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RollViewTest {

    @Test
    public void rollViewForValidRollIsSpaceSeparatedStringOfIndividualDice() throws Exception {
        HandOfDice handOfDice = HandOfDice.of(1, 2, 3, 4, 5);

        String rollView = RollView.forScoreboard(handOfDice);

        assertThat(rollView)
                .isEqualTo("1 2 3 4 5");
    }

    @Test
    public void rollViewForNonExistentRollIsEmptyString() throws Exception {
        HandOfDice handOfDice = HandOfDice.empty();

        String rollView = RollView.forScoreboard(handOfDice);

        assertThat(rollView)
                .isEmpty();
    }

}
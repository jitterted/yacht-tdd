package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.DiceRoll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class KeepIndexToDiceTest {

    @Test
    public void diceIndexesTranslatedToDiceValues() throws Exception {
        Keep keep = new Keep();
        keep.setDiceIndexesToKeep(List.of(0, 4));
        DiceRoll diceRoll = DiceRoll.of(4, 2, 6, 3, 4);

        List<Integer> diceValues = keep.diceValuesFrom(diceRoll);

        assertThat(diceValues)
                .containsExactlyInAnyOrder(4, 4);
    }

    @Test
    public void emptyDiceIndexesTranslatesToEmptyList() throws Exception {
        Keep keep = new Keep();
        keep.setDiceIndexesToKeep(Collections.emptyList());
        DiceRoll diceRoll = DiceRoll.of(4, 2, 6, 3, 4);

        List<Integer> diceValues = keep.diceValuesFrom(diceRoll);

        assertThat(diceValues)
                .isEmpty();
    }
    // TSTTCPW The Simplest (straightforwardist) Thing That Could Possibly Work
    // YAGNI You Ain't Gonna Need It
}
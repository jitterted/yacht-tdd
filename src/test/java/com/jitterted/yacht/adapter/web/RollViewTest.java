package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.DiceRoll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RollViewTest {

  @Test
  public void rollViewForValidRollIsSpaceSeparatedStringOfIndividualDice() throws Exception {
    DiceRoll diceRoll = DiceRoll.of(1, 2, 3, 4, 5);

    String rollView = RollView.forScoreboard(diceRoll);

    assertThat(rollView)
        .isEqualTo("1 2 3 4 5");
  }

  @Test
  public void rollViewForNonExistentRollIsEmptyString() throws Exception {
    DiceRoll diceRoll = DiceRoll.empty();

    String rollView = RollView.forScoreboard(diceRoll);

    assertThat(rollView)
        .isEmpty();
  }

}
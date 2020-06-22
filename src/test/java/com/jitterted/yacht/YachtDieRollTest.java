package com.jitterted.yacht;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtDieRollTest {
  
  // Rule #1: Never write/change production/feature code without a failing test
  // Rule #2: Write the absolute tiniest/easiest/straightforwardist amount of code to get the test to pass
  
  @Test
  public void rollFiveDiceResultsIn12345() throws Exception {
    // Given
    Yacht yacht = new Yacht(new DieRoller() {
      private int die = 0;
      @Override
      public int roll() {
        return ++die;
      }
    });

    // When
    String result = yacht.rollDice();

    // Then
    assertThat(result)
        .isEqualTo("12345");
  }
  
  @Test
  public void rollDiceResultsInFiveDice() throws Exception {
    Yacht yacht = new Yacht(new RandomDieRoller());

    String result = yacht.rollDice();

    assertThat(result)
        .hasSize(5);
  }
}

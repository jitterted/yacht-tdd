package com.jitterted;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringTest {

  @Test
  public void rollOf23456ResultsInScoreOfZeroForOnesCategory() throws Exception {
    Yacht yacht = new Yacht(new RandomDieRoller());

    int score = yacht.scoreAsOnes("23456");

    assertThat(score)
        .isZero();
  }

  @Test
  public void rollOf12345ResultsInScoreOf1ForOnesCategory() throws Exception {
    Yacht yacht = new Yacht(new RandomDieRoller());

    int score = yacht.scoreAsOnes("12345");

    assertThat(score)
        .isEqualTo(1);
  }
  
  @Test
  public void rollOf11111ResultsInScoreOf5ForOnesCategory() throws Exception {
    Yacht yacht = new Yacht(new RandomDieRoller());

    int score = yacht.scoreAsOnes("11111");

    assertThat(score)
        .isEqualTo(5);
  }
}

package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringYachtFiveOfAKindTest {

  @Test
  public void rollOf55556ScoresAs0() throws Exception {
    YachtScorer yachtScorer = new YachtScorer();

    int score = yachtScorer.scoreAsYacht(DiceRoll.of(5, 5, 5, 5, 6));

    assertThat(score)
        .isZero();
  }

  @Test
  public void rollOf55555ScoresAs25() throws Exception {
    YachtScorer yachtScorer = new YachtScorer();

    int score = yachtScorer.scoreAsYacht(DiceRoll.of(5, 5, 5, 5, 5));

    assertThat(score)
        .isEqualTo(5 + 5 + 5 + 5 + 5);
  }

}

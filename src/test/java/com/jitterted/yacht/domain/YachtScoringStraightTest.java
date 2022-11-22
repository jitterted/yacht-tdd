package com.jitterted.yacht.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringStraightTest {

    private YachtScorer yachtScorer;

    @BeforeEach
    void createYachtScorer() {
        yachtScorer = new YachtScorer();
    }

    @Test
    public void rollOf13456ScoredAsLittleStraightResultsIn0() throws Exception {
        int score = yachtScorer.scoreAsLittleStraight(DiceRoll.of(1, 3, 4, 5, 6));

        assertThat(score)
                .isZero();
    }

    @Test
    public void rollOf12345ScoredAsLittleStraightResultsIn30() throws Exception {
        int score = yachtScorer.scoreAsLittleStraight(DiceRoll.of(1, 2, 3, 4, 5));

        assertThat(score)
                .isEqualTo(30);
    }

    @Test
    public void rollOfLittleStraightOutOfOrderScoredAsLittleStraightResultsIn30() throws Exception {
        int score = yachtScorer.scoreAsLittleStraight(DiceRoll.of(5, 1, 4, 2, 3));

        assertThat(score)
                .isEqualTo(30);
    }

    @Test
    public void rollOfBigStraightInOrderScoredAsBigStraightResultsIn30() throws Exception {
        int score = yachtScorer.scoreAsBigStraight(DiceRoll.of(2, 3, 4, 5, 6));

        assertThat(score)
                .isEqualTo(30);
    }

    @Test
    public void rollOf12345ScoredAsBigStraightResultsIn0() throws Exception {
        int score = yachtScorer.scoreAsBigStraight(DiceRoll.of(1, 2, 3, 4, 5));

        assertThat(score)
                .isZero();

    }
}

package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class YachtScoringFullHouseTest {

    @Test
    public void straightRollScoresZero() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFullHouse(HandOfDice.of(1, 2, 3, 4, 5));

        assertThat(score)
                .isZero();
    }

    /**
     * This is one of two cases for not being a full house, but one of the dice occurs twice or more
     */
    @Test
    public void rollOf66554ScoresAs0() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFullHouse(HandOfDice.of(6, 6, 5, 5, 4));

        assertThat(score)
                .isZero();
    }

    /**
     * This is one of two cases for not being a full house, but one of the dice occurs twice or more
     */
    @Test
    public void rollOf51111ScoreAs0() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFullHouse(HandOfDice.of(5, 1, 1, 1, 1));

        assertThat(score)
                .isZero();
    }

    @Test
    public void rollOf33355ScoresAs19() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFullHouse(HandOfDice.of(3, 3, 3, 5, 5));

        assertThat(score)
                .isEqualTo(3 + 3 + 3 + 5 + 5);
    }

    @Test
    public void rollOf22666ScoresAs22() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFullHouse(HandOfDice.of(2, 2, 6, 6, 6));

        assertThat(score)
                .as("Should be scored as 2 + 2 + 6 + 6 + 6")
                .isEqualTo(2 + 2 + 6 + 6 + 6);
    }

    @Test
    public void rollOf55555ScoresAs0() throws Exception {
        YachtScorer yachtScorer = new YachtScorer();

        int score = yachtScorer.scoreAsFullHouse(HandOfDice.of(5, 5, 5, 5, 5));

        assertThat(score)
                .as("A Yacht (all five dice same number) cannot be scored on Full House, i.e., is zero")
                .isZero();
    }
}

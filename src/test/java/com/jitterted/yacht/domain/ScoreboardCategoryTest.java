package com.jitterted.yacht.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ScoreboardCategoryTest {

    @Test
    public void newScoreboardWithNoRollsAssignedReturnsScoredCategoriesWithNoDiceRolls() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        assertThat(scoreboard.scoredCategories())
                .hasSize(ScoreCategory.values().length);
    }

    @Test
    public void newScoreboardIsEmpty() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        assertThat(scoreboard.isEmpty())
                .isTrue();
    }

    @Test
    public void scoreboardWithAssignedCategoryIsNotEmpty() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        DiceRoll diceRoll = DiceRoll.of(4, 4, 4, 3, 4);
        scoreboard.scoreAs(ScoreCategory.FOURS, diceRoll);

        assertThat(scoreboard.isEmpty())
                .isFalse();
    }

    @Test
    public void assignSingleRollToScoreboardReturnsScoreForThatCategory() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        DiceRoll diceRoll = DiceRoll.of(6, 4, 4, 3, 4);
        scoreboard.scoreAs(ScoreCategory.FOURS, diceRoll);

        assertThat(scoreboard.scoredCategories())
                .contains(new ScoredCategory(ScoreCategory.FOURS, DiceRoll.of(6, 4, 4, 3, 4), 12));
    }

    @Test
    public void assignTwoRollsToSeparateCategoriesReturnsTwoScoredCategories() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        DiceRoll diceRollSixes = DiceRoll.of(6, 4, 4, 3, 4);
        scoreboard.scoreAs(ScoreCategory.SIXES, diceRollSixes);
        DiceRoll diceRollFives = DiceRoll.of(5, 4, 5, 3, 4);
        scoreboard.scoreAs(ScoreCategory.FIVES, diceRollFives);

        assertThat(scoreboard.scoredCategories())
                .contains(
                        new ScoredCategory(ScoreCategory.SIXES, diceRollSixes, 6),
                        new ScoredCategory(ScoreCategory.FIVES, diceRollFives, 10));
    }

}
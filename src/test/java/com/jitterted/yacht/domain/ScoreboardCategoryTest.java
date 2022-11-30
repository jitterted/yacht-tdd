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

        HandOfDice handOfDice = HandOfDice.of(4, 4, 4, 3, 4);
        scoreboard.scoreAs(ScoreCategory.FOURS, handOfDice);

        assertThat(scoreboard.isEmpty())
                .isFalse();
    }

    @Test
    public void assignSingleRollToScoreboardReturnsScoreForThatCategory() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        HandOfDice handOfDice = HandOfDice.of(6, 4, 4, 3, 4);
        scoreboard.scoreAs(ScoreCategory.FOURS, handOfDice);

        assertThat(scoreboard.scoredCategories())
                .contains(new ScoredCategory(ScoreCategory.FOURS, HandOfDice.of(6, 4, 4, 3, 4), 12));
    }

    @Test
    public void assignTwoRollsToSeparateCategoriesReturnsTwoScoredCategories() throws Exception {
        Scoreboard scoreboard = new Scoreboard();

        HandOfDice handOfDiceSixes = HandOfDice.of(6, 4, 4, 3, 4);
        scoreboard.scoreAs(ScoreCategory.SIXES, handOfDiceSixes);
        HandOfDice handOfDiceFives = HandOfDice.of(5, 4, 5, 3, 4);
        scoreboard.scoreAs(ScoreCategory.FIVES, handOfDiceFives);

        assertThat(scoreboard.scoredCategories())
                .contains(
                        new ScoredCategory(ScoreCategory.SIXES, handOfDiceSixes, 6),
                        new ScoredCategory(ScoreCategory.FIVES, handOfDiceFives, 10));
    }

}
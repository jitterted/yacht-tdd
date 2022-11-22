package com.jitterted.yacht.domain;

import com.jitterted.yacht.adapter.out.dieroller.RandomDieRoller;
import com.jitterted.yacht.application.DiceRoller;
import org.junit.jupiter.api.Test;

import static com.jitterted.yacht.domain.ScoreCategory.FULLHOUSE;
import static org.assertj.core.api.Assertions.*;

public class GameCategoryAssignmentTest {

    private static final ScoreCategory ARBITRARY_SCORE_CATEGORY = FULLHOUSE;

    @Test
    public void newRollThenLastRollIsNotYetAssignedToCategory() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);
        game.rollDice(diceRoller.roll());

        assertThat(game.roundCompleted())
                .isFalse();
    }

    @Test
    public void newRollWhenAssignedThenRollIsAssignedToCategory() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);
        game.rollDice(diceRoller.roll());

        game.assignRollTo(ARBITRARY_SCORE_CATEGORY);

        assertThat(game.roundCompleted())
                .isTrue();
    }

    @Test
    public void newRollAfterAssignmentWhenRollAgainThenRollIsNotAssignedToCategory() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);
        game.rollDice(diceRoller.roll());
        game.assignRollTo(ARBITRARY_SCORE_CATEGORY);

        game.rollDice(diceRoller.roll());

        assertThat(game.roundCompleted())
                .isFalse();
    }

    @Test
    public void newRollAfterAssignmentThenShouldNotBeAbleToReRoll() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);
        game.rollDice(diceRoller.roll());

        game.assignRollTo(ARBITRARY_SCORE_CATEGORY);

        assertThat(game.canReRoll())
                .isFalse();
    }

    @Test
    public void newGameThenGameIsNotOver() throws Exception {
        Game game = new Game(new DiceRoller(new RandomDieRoller()));

        assertThat(game.isOver())
                .isFalse();
    }

    @Test
    public void assigningToAllCategoriesEndsTheGame() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);

        assignRollToAllCategories(game, diceRoller);

        assertThat(game.isOver())
                .isTrue();
    }

    @Test
    public void assignedCategoryCanNotBeAssignedToAgain() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);
        game.rollDice(diceRoller.roll());
        game.assignRollTo(ARBITRARY_SCORE_CATEGORY);
        game.rollDice(diceRoller.roll());

        assertThatThrownBy(() -> game.assignRollTo(ARBITRARY_SCORE_CATEGORY))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void assignedRollCanNotBeReAssigned() throws Exception {
        DiceRoller diceRoller = new DiceRoller(new RandomDieRoller());
        Game game = new Game(diceRoller);
        game.rollDice(diceRoller.roll());
        game.assignRollTo(ScoreCategory.FOURS);

        assertThatThrownBy(() -> {
            game.assignRollTo(FULLHOUSE);
        }).isInstanceOf(IllegalStateException.class);
    }

    private void assignRollToAllCategories(Game game, DiceRoller diceRoller) {
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            game.rollDice(diceRoller.roll());
            game.assignRollTo(scoreCategory);
        }
    }
}

package com.jitterted.yacht.application;

import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;

import java.util.List;

public class GameService {
    private final ScoreCategoryNotifier scoreCategoryNotifier;
    private final DiceRoller diceRoller;

    private Game game;

    public GameService(DiceRoller diceRoller,
                       ScoreCategoryNotifier scoreCategoryNotifier) {
        this.diceRoller = diceRoller;
        this.scoreCategoryNotifier = scoreCategoryNotifier;
    }

    public void start() {
        game = new Game();
    }

    public void rollDice() {
        game.rollDice(diceRoller.roll());
    }

    public DiceRoll lastRoll() {
        return game.lastRoll();
    }

    public boolean canReRoll() {
        return game.canReRoll();
    }

    public boolean roundCompleted() {
        return game.roundCompleted();
    }

    public void reRoll(List<Integer> keptDice) {
        game.reRoll(diceRoller.reRoll(keptDice));
    }

    public void assignRollTo(ScoreCategory scoreCategory) {
        game.assignRollTo(scoreCategory);
        scoreCategoryNotifier.rollAssigned(null, -1, null);
    }

    public boolean isOver() {
        return game.isOver();
    }

    public List<ScoredCategory> scoredCategories() {
        return game.scoredCategories();
    }

    public int score() {
        return game.score();
    }
}

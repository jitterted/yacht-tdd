package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;

import java.util.List;

public class GameService {
    private final Game game;

    public GameService(Game game) {
        this.game = game;
    }

    public void start() {
        game.start();
    }

    public void rollDice() {
        game.rollDice();
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
        game.reRoll(keptDice);
    }

    public void assignRollTo(ScoreCategory scoreCategory) {
        game.assignRollTo(scoreCategory);
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

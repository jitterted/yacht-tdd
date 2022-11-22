package com.jitterted.yacht.domain;

import java.util.List;

public class Game {

    private final Scoreboard scoreboard = new Scoreboard();

    private DiceRoll lastRoll;

    private Rolls rolls = Rolls.start();

    private boolean roundCompleted;

    public Game() {
        lastRoll = DiceRoll.empty();
        roundCompleted = true;
    }

    public void rollDice(DiceRoll diceRoll) {
        roundCompleted = false;
        rolls = Rolls.start();
        lastRoll = diceRoll;
    }

    public void reRoll(DiceRoll diceRoll) {
        requireRerollsRemaining();
        rolls.increment();
        lastRoll = diceRoll;
    }

    private void requireRerollsRemaining() {
        if (!canReRoll()) {
            throw new TooManyRollsException();
        }
    }

    public DiceRoll lastRoll() {
        return lastRoll;
    }

    public int score() {
        return scoreboard.score();
    }

    public void assignRollTo(ScoreCategory scoreCategory) {
        requireRollNotYetAssigned();
        scoreboard.scoreAs(scoreCategory, lastRoll);
        roundCompleted = true;
    }

    private void requireRollNotYetAssigned() {
        if (roundCompleted) {
            throw new IllegalStateException();
        }
    }

    public List<ScoredCategory> scoredCategories() {
        return scoreboard.scoredCategories();
    }

    public boolean canReRoll() {
        if (roundCompleted()) {
            return false;
        }
        return rolls.canReRoll();
    }

    public boolean roundCompleted() {
        return roundCompleted;
    }

    public boolean isOver() {
        return scoreboard.isComplete();
    }

}

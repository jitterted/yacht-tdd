package com.jitterted.yacht.domain;

import java.util.List;

public class Game {

    private final Scoreboard scoreboard = new Scoreboard();

    private HandOfDice lastRoll;

    private Rolls rolls = Rolls.start();

    private boolean roundCompleted;

    public Game() {
        lastRoll = HandOfDice.empty();
        roundCompleted = true;
    }

    public void diceRolled(HandOfDice handOfDice) {
        roundCompleted = false;
        rolls = Rolls.start();
        lastRoll = handOfDice;
    }

    public void diceReRolled(HandOfDice handOfDice) {
        requireRerollsRemaining();
        rolls.increment();
        lastRoll = handOfDice;
    }

    private void requireRerollsRemaining() {
        if (!canReRoll()) {
            throw new TooManyRollsException();
        }
    }

    public HandOfDice lastRoll() {
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

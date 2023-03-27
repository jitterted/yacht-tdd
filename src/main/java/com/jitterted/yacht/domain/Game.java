package com.jitterted.yacht.domain;

import java.util.List;

public class Game {

    private final Scoreboard scoreboard;

    private HandOfDice currentHand;

    private Rolls rolls = Rolls.start();

    private boolean roundCompleted;

    public Game() {
        currentHand = HandOfDice.unassigned();
        roundCompleted = true;
        scoreboard = new Scoreboard();
    }

    private Game(Snapshot snapshot) {
        roundCompleted = snapshot.roundCompleted();
        rolls = new Rolls(snapshot.rolls());
        currentHand = snapshot.currentHand();
        scoreboard = Scoreboard.from(snapshot.scoreboard());
    }

    public static Game from(Snapshot snapshot) {
        return new Game(snapshot);
    }

    public void diceRolled(HandOfDice handOfDice) {
        roundCompleted = false;
        rolls = Rolls.start();
        currentHand = handOfDice;
    }

    public void diceReRolled(HandOfDice handOfDice) {
        requireRerollsRemaining();
        rolls.increment();
        currentHand = handOfDice;
    }

    private void requireRerollsRemaining() {
        if (!canReRoll()) {
            throw new TooManyRollsException();
        }
    }

    public HandOfDice currentHand() {
        return currentHand;
    }

    public int score() {
        return scoreboard.score();
    }

    public void assignCurrentHandTo(ScoreCategory scoreCategory) {
        requireHandNotYetAssigned();
        scoreboard.scoreAs(scoreCategory, currentHand);
        roundCompleted = true;
    }

    private void requireHandNotYetAssigned() {
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

    public Snapshot snapshot() {
        return new Snapshot(rolls.rolls(), roundCompleted,
                            currentHand,
                            scoreboard.memento());
    }

    public record Snapshot(int rolls,
                           boolean roundCompleted,
                           HandOfDice currentHand,
                           Scoreboard.Snapshot scoreboard) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Game game = (Game) o;

        if (roundCompleted != game.roundCompleted) {
            return false;
        }
        if (!scoreboard.equals(game.scoreboard)) {
            return false;
        }
        if (!currentHand.equals(game.currentHand)) {
            return false;
        }
        return rolls.equals(game.rolls);
    }

    @Override
    public int hashCode() {
        int result = scoreboard.hashCode();
        result = 31 * result + currentHand.hashCode();
        result = 31 * result + rolls.hashCode();
        result = 31 * result + (roundCompleted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Game{" +
                "scoreboard=" + scoreboard +
                ", lastRoll=" + currentHand +
                ", rolls=" + rolls +
                ", roundCompleted=" + roundCompleted +
                '}';
    }
}

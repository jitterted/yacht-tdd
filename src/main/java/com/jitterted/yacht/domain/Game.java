package com.jitterted.yacht.domain;

import java.util.List;

public class Game {

    private final Scoreboard scoreboard;

    private HandOfDice lastRoll;

    private Rolls rolls = Rolls.start();

    private boolean roundCompleted;

    public Game() {
        lastRoll = HandOfDice.unassigned();
        roundCompleted = true;
        scoreboard = new Scoreboard();
    }

    private Game(Snapshot snapshot) {
        roundCompleted = snapshot.roundCompleted();
        rolls = new Rolls(snapshot.rolls());
        lastRoll = HandOfDice.from(snapshot.currentHand());
        scoreboard = Scoreboard.from(snapshot.scoreboard());
    }

    public static Game from(Snapshot snapshot) {
        return new Game(snapshot);
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

    public HandOfDice currentHand() {
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

    public Snapshot memento() {
        return new Snapshot(roundCompleted,
                            rolls.rolls(),
                            lastRoll.stream().toList(),
                            scoreboard.memento());
    }

    public record Snapshot(boolean roundCompleted,
                           int rolls,
                           List<Integer> currentHand,
                           Scoreboard.Memento scoreboard) {
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
        if (!lastRoll.equals(game.lastRoll)) {
            return false;
        }
        return rolls.equals(game.rolls);
    }

    @Override
    public int hashCode() {
        int result = scoreboard.hashCode();
        result = 31 * result + lastRoll.hashCode();
        result = 31 * result + rolls.hashCode();
        result = 31 * result + (roundCompleted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Game{" +
                "scoreboard=" + scoreboard +
                ", lastRoll=" + lastRoll +
                ", rolls=" + rolls +
                ", roundCompleted=" + roundCompleted +
                '}';
    }
}

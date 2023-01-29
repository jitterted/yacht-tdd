package com.jitterted.yacht.domain;

import java.util.List;
import java.util.Map;

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

    private Game(Memento memento) {
        roundCompleted = memento.roundCompleted();
        rolls = new Rolls(memento.rolls());
        lastRoll = HandOfDice.from(memento.lastRoll());
        Scoreboard.Memento scoreboardMemento = new Scoreboard.Memento(memento.scoreboard);
        scoreboard = new Scoreboard(scoreboardMemento);
    }

    public static Game from(Memento memento) {
        return new Game(memento);
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

    public Memento memento() {
        return new Memento(roundCompleted,
                           rolls.rolls(),
                           lastRoll.stream().toList(),
                           scoreboard.memento().scoredCategoryHandMap());
    }

    public record Memento(boolean roundCompleted,
                          int rolls,
                          List<Integer> lastRoll,
                          Map<ScoreCategory, List<Integer>> scoreboard) {
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

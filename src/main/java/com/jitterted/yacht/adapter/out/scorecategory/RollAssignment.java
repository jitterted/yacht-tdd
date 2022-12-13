package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;

public class RollAssignment {
    private final HandOfDice handOfDice;
    private final int score;
    private final ScoreCategory scoreCategory;

    public RollAssignment(HandOfDice handOfDice, int score, ScoreCategory scoreCategory) {
        this.handOfDice = handOfDice;
        this.score = score;
        this.scoreCategory = scoreCategory;
    }

    public HandOfDice handOfDice() {
        return handOfDice;
    }

    public int score() {
        return score;
    }

    public ScoreCategory scoreCategory() {
        return scoreCategory;
    }

    @Override
    public String toString() {
        return "RollAssignment{" +
                "handOfDice=" + handOfDice +
                ", score=" + score +
                ", scoreCategory=" + scoreCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RollAssignment that = (RollAssignment) o;

        if (score != that.score) {
            return false;
        }
        if (!handOfDice.equals(that.handOfDice)) {
            return false;
        }
        return scoreCategory == that.scoreCategory;
    }

    @Override
    public int hashCode() {
        int result = handOfDice.hashCode();
        result = 31 * result + score;
        result = 31 * result + scoreCategory.hashCode();
        return result;
    }
}

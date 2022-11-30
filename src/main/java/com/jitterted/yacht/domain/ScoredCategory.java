package com.jitterted.yacht.domain;

public class ScoredCategory {
    private final ScoreCategory scoreCategory;
    private final HandOfDice handOfDice;
    private final int score;

    public ScoredCategory(ScoreCategory scoreCategory, HandOfDice handOfDice, int score) {
        this.scoreCategory = scoreCategory;
        this.handOfDice = handOfDice;
        this.score = score;
    }

    public static ScoredCategory createUnassignedScoredCategoryFor(ScoreCategory scoreCategory) {
        return new ScoredCategory(scoreCategory, HandOfDice.empty(), 0);
    }

    public ScoreCategory scoreCategory() {
        return scoreCategory;
    }

    public HandOfDice diceRoll() {
        return handOfDice;
    }

    public int score() {
        return score;
    }

    public boolean isAssigned() {
        return !diceRoll().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScoredCategory that = (ScoredCategory) o;

        if (score != that.score) {
            return false;
        }
        if (scoreCategory != that.scoreCategory) {
            return false;
        }
        return handOfDice.equals(that.handOfDice);
    }

    @Override
    public int hashCode() {
        int result = scoreCategory.hashCode();
        result = 31 * result + handOfDice.hashCode();
        result = 31 * result + score;
        return result;
    }

    @Override
    public String toString() {
        return "ScoredCategory: " +
                "scoreCategory=" + scoreCategory +
                ", handOfDice=" + handOfDice +
                ", score=" + score;
    }
}

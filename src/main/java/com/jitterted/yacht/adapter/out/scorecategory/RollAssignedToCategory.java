package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;

import java.util.stream.Collectors;

class RollAssignedToCategory {
    private final String roll;
    private final String score;
    private final String category;

    public RollAssignedToCategory(String roll, String score, String category) {
        this.roll = roll;
        this.score = score;
        this.category = category;
    }

    static RollAssignedToCategory from(HandOfDice handOfDice,
                                       int score,
                                       ScoreCategory scoreCategory) {
        String rollString = handOfDice.stream()
                                      .map(Object::toString)
                                      .collect(Collectors.joining(" "));
        String scoreString = String.valueOf(score);
        String categoryString = scoreCategory.toString();
        return new RollAssignedToCategory(rollString,
                                          scoreString,
                                          categoryString
        );
    }

    public String getRoll() {
        return roll;
    }

    public String getScore() {
        return score;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RollAssignedToCategory that = (RollAssignedToCategory) o;

        if (!roll.equals(that.roll)) {
            return false;
        }
        if (!score.equals(that.score)) {
            return false;
        }
        return category.equals(that.category);
    }

    @Override
    public int hashCode() {
        int result = roll.hashCode();
        result = 31 * result + score.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RollAssignedToCategory{" +
                "roll='" + roll + '\'' +
                ", score='" + score + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}

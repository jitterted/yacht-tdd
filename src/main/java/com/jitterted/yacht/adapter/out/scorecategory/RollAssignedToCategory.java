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
}

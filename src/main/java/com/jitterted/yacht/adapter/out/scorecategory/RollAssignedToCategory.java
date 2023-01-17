package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;

import java.util.stream.Collectors;

record RollAssignedToCategory(String roll, String score, String category) {

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

}

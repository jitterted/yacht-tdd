package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;

// Missing Domain concept? -> this might be AssignedRoll as a Value Object (Type)
public record RollAssignment(HandOfDice handOfDice,
                             int score,
                             ScoreCategory scoreCategory) {
}

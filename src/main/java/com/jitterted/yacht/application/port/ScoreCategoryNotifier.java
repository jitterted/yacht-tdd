package com.jitterted.yacht.application.port;

import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;

public interface ScoreCategoryNotifier {
    void rollAssigned(HandOfDice handOfDice, int score, ScoreCategory scoreCategory);
}

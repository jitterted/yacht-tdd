package com.jitterted.yacht.application.port;

import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.ScoreCategory;

public interface ScoreCategoryNotifier {
    void rollAssigned(DiceRoll diceRoll, int score, ScoreCategory scoreCategory);
}

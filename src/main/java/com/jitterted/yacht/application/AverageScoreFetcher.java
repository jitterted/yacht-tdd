package com.jitterted.yacht.application;

import com.jitterted.yacht.domain.ScoreCategory;

public class AverageScoreFetcher {
    double averageFor(ScoreCategory scoreCategory) {
        if (scoreCategory == ScoreCategory.BIGSTRAIGHT) {
            return 12.0;
        } else {
            return 20.0;
        }
    }
}
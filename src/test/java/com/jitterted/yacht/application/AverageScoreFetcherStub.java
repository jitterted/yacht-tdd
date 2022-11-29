package com.jitterted.yacht.application;

import com.jitterted.yacht.application.port.AverageScoreFetcher;
import com.jitterted.yacht.domain.ScoreCategory;

public class AverageScoreFetcherStub implements AverageScoreFetcher {
    @Override
    public double averageFor(ScoreCategory scoreCategory) {
        if (scoreCategory == ScoreCategory.BIGSTRAIGHT) {
            return 12.0;
        } else {
            return 20.0;
        }
    }
}
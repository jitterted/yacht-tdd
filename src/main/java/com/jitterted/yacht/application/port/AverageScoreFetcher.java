package com.jitterted.yacht.application.port;

import com.jitterted.yacht.domain.ScoreCategory;

public interface AverageScoreFetcher {
    double averageFor(ScoreCategory scoreCategory);
}

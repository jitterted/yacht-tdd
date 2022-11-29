package com.jitterted.yacht.adapter.out.averagescore;

import com.jitterted.yacht.application.port.AverageScoreFetcher;
import com.jitterted.yacht.domain.ScoreCategory;

public class HttpAverageScoreFetcher implements AverageScoreFetcher {
    @Override
    public double averageFor(ScoreCategory scoreCategory) {
        throw new UnsupportedOperationException();
    }
}

package com.jitterted.yacht.adapter.out.averagescore;

import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class HttpAverageScoreFetcherTest {

    @Test
    void nullFetcherReturnsDefaultValue() {
        HttpAverageScoreFetcher fetcher =
                HttpAverageScoreFetcher.createNull();

        assertThat(fetcher.averageFor(ScoreCategory.FOURS))
                .isEqualTo(42.0);
    }
}
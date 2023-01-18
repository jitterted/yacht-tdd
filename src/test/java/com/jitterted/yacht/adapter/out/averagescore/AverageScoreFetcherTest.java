package com.jitterted.yacht.adapter.out.averagescore;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.JsonHttpClient;
import com.jitterted.yacht.adapter.out.JsonHttpRequest;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AverageScoreFetcherTest {
    @Test
    void fetchesAverageFromExternalService() {
        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull(Map.of(
                "http://localhost:8080/api/averages?scoreCategory=FIVES",
                new CategoryAverage("FIVES", 25.0)));
        OutputTracker<JsonHttpRequest> tracker = jsonHttpClient.trackRequests();
        AverageScoreFetcher averageScoreFetcher =
                new AverageScoreFetcher(jsonHttpClient);

        double average = averageScoreFetcher.averageFor(ScoreCategory.FIVES);

        assertThat(tracker.output())
                .containsExactly(JsonHttpRequest.createGet(
                        "http://localhost:8080/api/averages?scoreCategory=FIVES")
                );

        assertThat(average)
                .isEqualTo(25.0);
    }

    @Test
    void nulledFetcherReturnsDefaultValue() {
        AverageScoreFetcher fetcher =
                AverageScoreFetcher.createNull();

        assertThat(fetcher.averageFor(ScoreCategory.FOURS))
                .isEqualTo(42.0);
        assertThat(fetcher.averageFor(ScoreCategory.FULLHOUSE))
                .isEqualTo(42.0);
        assertThat(fetcher.averageFor(ScoreCategory.BIGSTRAIGHT))
                .isEqualTo(42.0);
    }

    @Test
    void nulledFetcherReturnsConfiguredValue() {
        AverageScoreFetcher fetcher =
                AverageScoreFetcher.createNull(
                        Map.of(ScoreCategory.FOURS, 1.0,
                               ScoreCategory.FULLHOUSE, 2.0,
                               ScoreCategory.BIGSTRAIGHT, 3.0)
                );

        assertThat(fetcher.averageFor(ScoreCategory.FOURS))
                .isEqualTo(1.0);
    }

    @Test
    void whenConfiguredNulledFetcherReturnsDefaultForUnconfiguredValues() {
        AverageScoreFetcher fetcher =
                AverageScoreFetcher.createNull(
                        Map.of(ScoreCategory.FOURS, 1.0)
                );

        assertThat(fetcher.averageFor(ScoreCategory.FULLHOUSE))
                .isEqualTo(42.0);
    }
}
package com.jitterted.yacht.adapter.out.averagescore;

import com.jitterted.yacht.adapter.out.JsonHttpClient;
import com.jitterted.yacht.domain.ScoreCategory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AverageScoreFetcher {
    private static final String YACHT_AVERAGE_API_URI =
            "http://localhost:8080/api/averages?scoreCategory={scoreCategory}";

    private final JsonHttpClient jsonHttpClient;

    AverageScoreFetcher(JsonHttpClient jsonHttpClient) {
        this.jsonHttpClient = jsonHttpClient;
    }

    public static AverageScoreFetcher create() {
        return new AverageScoreFetcher(JsonHttpClient.create());
    }

    public static AverageScoreFetcher createNull() {
        return createNull(Collections.emptyMap());
    }

    public static AverageScoreFetcher createNull(Map<ScoreCategory, Double> map) {
        Map<String, Object> endpointsResponses = nulledHttpResponses(map);
        return new AverageScoreFetcher(JsonHttpClient.createNull(endpointsResponses));
    }

    public double averageFor(ScoreCategory scoreCategory) {
        CategoryAverage categoryAverage =
                jsonHttpClient.get(YACHT_AVERAGE_API_URI,
                                   CategoryAverage.class,
                                   scoreCategory.toString());
        return categoryAverage.getAverage();
    }

    private static Map<String, Object> nulledHttpResponses(Map<ScoreCategory, Double> fetcherResponses) {
        Map<String, Object> httpResponses = new HashMap<>();
        for (ScoreCategory scoreCategory : ScoreCategory.values()) {
            String url = "http://localhost:8080/api/averages?scoreCategory=" + scoreCategory;
            double value = fetcherResponses.getOrDefault(scoreCategory, 42.0);
            httpResponses.put(url, new CategoryAverage(scoreCategory.toString(), value));
        }
        return httpResponses;
    }

}

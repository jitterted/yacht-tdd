package com.jitterted.yacht.adapter.out.averagescore;

import com.jitterted.yacht.application.port.AverageScoreFetcher;
import com.jitterted.yacht.domain.ScoreCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpAverageScoreFetcher implements AverageScoreFetcher {
    private static final String YACHT_AVERAGE_API_URI =
            "http://localhost:8080/api/averages?scoreCategory={scoreCategory}";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public double averageFor(ScoreCategory scoreCategory) {
        ResponseEntity<CategoryAverage> entity = restTemplate.getForEntity(
                YACHT_AVERAGE_API_URI,
                CategoryAverage.class,
                scoreCategory.toString());
        return entity.getBody().getAverage();
    }
}

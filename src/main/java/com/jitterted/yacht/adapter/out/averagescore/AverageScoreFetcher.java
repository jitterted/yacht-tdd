package com.jitterted.yacht.adapter.out.averagescore;

import com.jitterted.yacht.domain.ScoreCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.NoSuchElementException;

public class AverageScoreFetcher {
    private static final String YACHT_AVERAGE_API_URI =
            "http://localhost:8080/api/averages?scoreCategory={scoreCategory}";

    private final RestTemplateWrapper restTemplate;

    public static AverageScoreFetcher create() {
        return new AverageScoreFetcher(new RealRestTemplate());
    }

    public static AverageScoreFetcher createNull() {
        return new AverageScoreFetcher(new StubbedRestTemplate());
    }

    public static AverageScoreFetcher createNull(Map<ScoreCategory, Double> map) {
        return new AverageScoreFetcher(new StubbedRestTemplate(map));
    }

    private AverageScoreFetcher(RestTemplateWrapper restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double averageFor(ScoreCategory scoreCategory) {
        ResponseEntityWrapper<CategoryAverage> entity =
                restTemplate.getForEntity(YACHT_AVERAGE_API_URI,
                                          CategoryAverage.class,
                                          scoreCategory.toString());
        return entity.getBody().getAverage();
    }


    //// --- NULLABLES BELOW ---


    interface RestTemplateWrapper {
        <T> ResponseEntityWrapper<T> getForEntity(String url, Class<T> responseType, Object... uriVariables);
    }

    private static class RealRestTemplate implements RestTemplateWrapper {
        private final RestTemplate restTemplate = new RestTemplate();

        public <T> ResponseEntityWrapper<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
            ResponseEntity<T> entity = restTemplate.getForEntity(
                    url,
                    responseType,
                    uriVariables);
            return new RealResponseEntity<T>(entity);
        }
    }

    private static class StubbedRestTemplate implements RestTemplateWrapper {
        private Map<ScoreCategory, Double> map;

        public StubbedRestTemplate() {
        }

        public StubbedRestTemplate(Map<ScoreCategory, Double> map) {
            this.map = map;
        }

        @Override
        public <T> ResponseEntityWrapper<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
            if (map == null) {
                return new StubbedResponseEntity<>(42.0);
            }

            String categoryString = (String) uriVariables[0];
            ScoreCategory scoreCategory = ScoreCategory.valueOf(categoryString);
            requireAverageFor(scoreCategory);

            return new StubbedResponseEntity<>(map.get(scoreCategory));
        }

        private void requireAverageFor(ScoreCategory scoreCategory) {
            if (!map.containsKey(scoreCategory)) {
                throw new NoSuchElementException("No average configured for " + scoreCategory + " in Null AverageScoreFetcher.");
            }
        }
    }

    interface ResponseEntityWrapper<T> {
        T getBody();
    }

    private static class RealResponseEntity<T> implements ResponseEntityWrapper<T> {
        private ResponseEntity<T> entity;

        RealResponseEntity(ResponseEntity<T> entity) {
            this.entity = entity;
        }

        public T getBody() {
            return this.entity.getBody();
        }
    }

    private static class StubbedResponseEntity<T> implements ResponseEntityWrapper<T> {

        private double average;

        public StubbedResponseEntity(double average) {
            this.average = average;
        }

        @Override
        public T getBody() {
            return (T) new CategoryAverage("Null Category", average);
        }
    }
}

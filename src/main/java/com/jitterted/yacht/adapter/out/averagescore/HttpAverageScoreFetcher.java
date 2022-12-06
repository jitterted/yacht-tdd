package com.jitterted.yacht.adapter.out.averagescore;

import com.jitterted.yacht.application.port.AverageScoreFetcher;
import com.jitterted.yacht.domain.ScoreCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpAverageScoreFetcher implements AverageScoreFetcher {
    private static final String YACHT_AVERAGE_API_URI =
            "http://localhost:8080/api/averages?scoreCategory={scoreCategory}";

    private final RestTemplateWrapper restTemplate;

    public static HttpAverageScoreFetcher create() {
        return new HttpAverageScoreFetcher(new RealRestTemplate());
    }

    public static HttpAverageScoreFetcher createNull() {
        return new HttpAverageScoreFetcher(new StubbedRestTemplate());
    }

    private HttpAverageScoreFetcher(RestTemplateWrapper restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
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
        @Override
        public <T> ResponseEntityWrapper<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
            return new StubbedResponseEntity<>();
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

        @Override
        public T getBody() {
            return (T) new CategoryAverage("Null Category", 42.0);
        }
    }
}

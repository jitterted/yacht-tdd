package com.jitterted.yacht.adapter.out.scorecategory;

import com.jitterted.yacht.adapter.OutputListener;
import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class HttpScoreCategoryNotifier implements ScoreCategoryNotifier {
    private static final URI YACHT_TRACKER_API_URI =
            URI.create("http://localhost:8080/api/scores");

    private final RestTemplateWrapper restTemplate;

    private final OutputListener<RollAssignment> outputListener = new OutputListener<>();

    public HttpScoreCategoryNotifier() {
        restTemplate = new RealRestTemplate();
    }

    public HttpScoreCategoryNotifier(RestTemplateWrapper restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static HttpScoreCategoryNotifier createNull() {
        return new HttpScoreCategoryNotifier(new DummyRestTemplate());        
    }

    @Override
    public void rollAssigned(HandOfDice handOfDice,
                             int score,
                             ScoreCategory scoreCategory) {
        RollAssignedToCategory rollAssignedToCategory = RollAssignedToCategory
                .from(handOfDice, score, scoreCategory);

        outputListener.emit(new RollAssignment(handOfDice, score, scoreCategory));

        restTemplate.postForEntity(YACHT_TRACKER_API_URI,
                                   rollAssignedToCategory,
                                   Void.class);
    }

    public OutputTracker<RollAssignment> trackAssignments() {
        return outputListener.createTracker();
    }


    //// --- NULLABLES BELOW ---


    interface RestTemplateWrapper {
        <T> void postForEntity(URI url,
                               Object request,
                               Class<T> responseType)
                throws RestClientException;
    }

    private static class RealRestTemplate implements RestTemplateWrapper {
        private final RestTemplate restTemplate = new RestTemplate();

        @Override
        public <T> void postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
            restTemplate.postForEntity(url, request, responseType);
        }
    }

    private static class DummyRestTemplate implements RestTemplateWrapper {

        @Override
        public <T> void postForEntity(URI url, Object request, Class<T> responseType) throws RestClientException {
            // do nothing
        }
    }
}

package com.jitterted.yacht.adapter.out;

import com.jitterted.yacht.adapter.OutputListener;
import com.jitterted.yacht.adapter.OutputTracker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class JsonHttpClient {

    private final RestTemplateWrapper restTemplateWrapper;
    private final OutputListener<JsonHttpRequest> listener = new OutputListener<>();

    public static JsonHttpClient create() {
        return new JsonHttpClient(new RealRestTemplate());
    }

    public static JsonHttpClient createNull() {
        return JsonHttpClient.createNull(Collections.emptyMap());
    }

    public static JsonHttpClient createNull(Map<String, Object> endpointsResponses) {
        return new JsonHttpClient(new StubbedRestTemplate(endpointsResponses));
    }

    private JsonHttpClient(RestTemplateWrapper restTemplateWrapper) {
        this.restTemplateWrapper = restTemplateWrapper;
    }

    public <R> R get(String urlTemplate,
                     Class<R> convertedResponseType,
                     String... urlVariables) {
        listener.emit(JsonHttpRequest.createGet(
                interpolateUrl(urlTemplate, urlVariables)));
        return restTemplateWrapper.getForEntity(urlTemplate,
                                                convertedResponseType,
                                                (Object[]) urlVariables)
                                  .getBody();
    }

    public void post(String url, Object body) {
        listener.emit(JsonHttpRequest.createPost(url, body));
        restTemplateWrapper.postForEntity(url,
                                          body,
                                          Void.class);
    }

    public OutputTracker<JsonHttpRequest> trackRequests() {
        return listener.createTracker();
    }

    private static String interpolateUrl(String urlTemplate, Object[] uriVariables) {
        return new DefaultUriBuilderFactory()
                .expand(urlTemplate, uriVariables)
                .toString();
    }


    interface RestTemplateWrapper {
        <T> ResponseEntityWrapper<T> getForEntity(String url, Class<T> responseType, Object... uriVariables);

        <T> void postForEntity(String url, Object request, Class<T> responseType);
    }

    interface ResponseEntityWrapper<T> {
        T getBody();
    }

    private static class RealRestTemplate implements RestTemplateWrapper {
        private final RestTemplate restTemplate = new RestTemplate();

        @Override
        public <T> ResponseEntityWrapper<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
            ResponseEntity<T> entity = restTemplate.getForEntity(
                    url,
                    responseType,
                    uriVariables);
            return new RealResponseEntity<T>(entity);
        }

        @Override
        public <T> void postForEntity(String url, Object request, Class<T> responseType) {
            restTemplate.postForEntity(url, request, responseType);
        }
    }

    private static class RealResponseEntity<T> implements ResponseEntityWrapper<T> {
        private final ResponseEntity<T> entity;

        RealResponseEntity(ResponseEntity<T> entity) {
            this.entity = entity;
        }

        public T getBody() {
            return this.entity.getBody();
        }
    }

    //    EMBEDDED STUB

    private static class StubbedRestTemplate implements RestTemplateWrapper {
        private final Map<String, Iterator<Object>> endpointsResponses;

        public StubbedRestTemplate(Map<String, Object> endpointsResponses) {
            this.endpointsResponses = new HashMap<>();
            for (Map.Entry<String, Object> entry : endpointsResponses.entrySet()) {
                this.endpointsResponses.put(entry.getKey(), normalizeResponses(entry));
            }
        }

        @SuppressWarnings("unchecked")
        private static Iterator<Object> normalizeResponses(Map.Entry<String, Object> entry) {
            if (entry.getValue() instanceof List) {
                // finite list of different responses
                return ((List<Object>) entry.getValue()).iterator();
            } else {
                // generate infinite list of the one response
                return Stream.generate(entry::getValue).iterator();
            }
        }

        @Override
        public <T> ResponseEntityWrapper<T> getForEntity(String url,
                                                         Class<T> responseType,
                                                         Object... uriVariables) {
            String interpolatedUrl = interpolateUrl(url, uriVariables);

            T response = nextResponse(interpolatedUrl);
            return new StubbedResponseEntity<>(response);
        }

        @Override
        public <T> void postForEntity(String url, Object request, Class<T> responseType) {
            // no-op
        }

        private <T> T nextResponse(String interpolatedUrl) {
            if (!endpointsResponses.containsKey(interpolatedUrl)) {
                throw new NoSuchElementException("URL not configured: " + interpolatedUrl);
            }

            @SuppressWarnings("unchecked")
            Iterator<T> response = (Iterator<T>) endpointsResponses.get(interpolatedUrl);
            if (!response.hasNext()) {
                throw new NoSuchElementException("No more responses configured for URL: "
                                                         + interpolatedUrl);
            }
            return response.next();
        }

    }


    private static class StubbedResponseEntity<T> implements ResponseEntityWrapper<T> {
        private final T configuredResponse;

        public StubbedResponseEntity(T configuredResponse) {
            this.configuredResponse = configuredResponse;
        }

        @Override
        public T getBody() {
            return configuredResponse;
        }
    }

}

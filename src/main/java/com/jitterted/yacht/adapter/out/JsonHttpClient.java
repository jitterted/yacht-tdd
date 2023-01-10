package com.jitterted.yacht.adapter.out;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class JsonHttpClient {

    private final RestTemplateWrapper restTemplateWrapper;

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
        return restTemplateWrapper.getForEntity(urlTemplate,
                                                convertedResponseType,
                                                (Object[]) urlVariables)
                                  .getBody();
    }

//    public void post(String urlTemplate, Object requestType) {
//        restTemplate.postForEntity(urlTemplate,
//                                   requestType,
//                                   Void.class);
//    }

    interface RestTemplateWrapper {
        <T> ResponseEntityWrapper<T> getForEntity(String url, Class<T> responseType, Object... uriVariables);
    }

    interface ResponseEntityWrapper<T> {
        T getBody();
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

    private static class RealResponseEntity<T> implements ResponseEntityWrapper<T> {
        private ResponseEntity<T> entity;

        RealResponseEntity(ResponseEntity<T> entity) {
            this.entity = entity;
        }

        public T getBody() {
            return this.entity.getBody();
        }
    }

    //    EMBEDDED STUB

    private static class StubbedRestTemplate implements RestTemplateWrapper {
        private Map<String, Object> endpointsResponses;

        public StubbedRestTemplate(Map<String, Object> endpointsResponses) {
            this.endpointsResponses = new HashMap<>();
            for (Map.Entry<String, Object> entry : endpointsResponses.entrySet()) {
                if (entry.getValue() instanceof List) {
                    this.endpointsResponses.put(entry.getKey(), ((List<?>) entry.getValue()).iterator());
                } else {
                    this.endpointsResponses.put(entry.getKey(), entry.getValue());
                }
            }
        }

        @Override
        public <T> ResponseEntityWrapper<T> getForEntity(String url,
                                                         Class<T> responseType,
                                                         Object... uriVariables) {
            String interpolatedUrl = new DefaultUriBuilderFactory()
                    .expand(url, uriVariables)
                    .toString();

            T configuredResponse = nextResponse(interpolatedUrl);
            return new StubbedResponseEntity<>(configuredResponse);
        }

        private <T> T nextResponse(String interpolatedUrl) {
            if (!endpointsResponses.containsKey(interpolatedUrl)) {
                throw new NoSuchElementException("URL not configured: " + interpolatedUrl);
            }

            Object configuredResponse = endpointsResponses.get(interpolatedUrl);
            if (configuredResponse instanceof Iterator) {
                return nextResponseFromIterator(interpolatedUrl, (Iterator<T>) configuredResponse);
            } else {
                return (T) configuredResponse;
            }
        }

        private <T> T nextResponseFromIterator(String interpolatedUrl, Iterator<T> responseIterator) {
            if (!responseIterator.hasNext()) {
                throw new NoSuchElementException("No more responses configured for URL: "
                                                         + interpolatedUrl);
            }
            return responseIterator.next();
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

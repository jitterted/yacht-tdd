package com.jitterted.yacht.adapter.out;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
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

        public StubbedRestTemplate() {
        }

        public StubbedRestTemplate(Map<String, Object> endpointsResponses) {
            this.endpointsResponses = endpointsResponses;
        }

        @Override
        public <T> ResponseEntityWrapper<T> getForEntity(String url,
                                                         Class<T> responseType,
                                                         Object... uriVariables) {
            if (endpointsResponses == null) {
                try {
                    T response = responseType.getConstructor().newInstance();
                    return new StubbedResponseEntity<>(response);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            String interpolatedUrl = new DefaultUriBuilderFactory()
                    .expand(url, uriVariables)
                    .toString();

            T configuredResponse;
            if (endpointsResponses.containsKey(interpolatedUrl)) {
                configuredResponse = (T) endpointsResponses.get(interpolatedUrl);
            } else {
                throw new NoSuchElementException("URL not configured: " + interpolatedUrl);
            }

            return new StubbedResponseEntity<>(configuredResponse);
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

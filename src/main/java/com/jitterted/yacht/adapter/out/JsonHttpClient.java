package com.jitterted.yacht.adapter.out;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;

public class JsonHttpClient {

    private final RestTemplateWrapper restTemplateWrapper;

    public static JsonHttpClient create() {
        return new JsonHttpClient(new RealRestTemplate());
    }

    public static JsonHttpClient createNull() {
        return new JsonHttpClient(new StubbedRestTemplate());
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
        @Override
        public <T> ResponseEntityWrapper<T> getForEntity(String url,
                                                         Class<T> responseType,
                                                         Object... uriVariables) {
            return new StubbedResponseEntity<>(responseType);
        }
    }

    private static class StubbedResponseEntity<T> implements ResponseEntityWrapper<T> {
        private final Class<T> responseType;

        public StubbedResponseEntity(Class<T> responseType) {
            this.responseType = responseType;
        }

        @Override
        public T getBody() {
            try {
                return responseType.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

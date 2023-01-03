package com.jitterted.yacht.adapter.out;

import org.springframework.web.client.RestTemplate;

public class JsonHttpClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public <R> R get(String urlTemplate,
                         Class<R> convertedResponseType,
                         String... urlVariables) {
        return restTemplate.getForEntity(urlTemplate,
                                         convertedResponseType,
                                         (Object[]) urlVariables)
                           .getBody();
    }

    public void post(String urlTemplate, Object requestType) {
        restTemplate.postForEntity(urlTemplate,
                                   requestType,
                                   Void.class);
    }


}

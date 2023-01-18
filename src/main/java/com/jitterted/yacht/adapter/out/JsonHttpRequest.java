package com.jitterted.yacht.adapter.out;

import org.springframework.http.HttpMethod;

import java.util.Objects;

public class JsonHttpRequest {
    private final HttpMethod httpMethod;
    private final String url;
    private final Object body;

    public static JsonHttpRequest createGet(String url) {
        return new JsonHttpRequest(HttpMethod.GET, url, null);
    }

    public static JsonHttpRequest createPost(String url, Object body) {
        return new JsonHttpRequest(HttpMethod.POST, url, body);
    }

    private JsonHttpRequest(HttpMethod httpMethod, String url, Object body) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.body = body;
    }

    public HttpMethod httpMethod() {
        return httpMethod;
    }

    public String url() {
        return url;
    }

    public Object body() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonHttpRequest that = (JsonHttpRequest) o;

        if (httpMethod != that.httpMethod) {
            return false;
        }
        if (!url.equals(that.url)) {
            return false;
        }
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        int result = httpMethod.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JsonHttpRequest{" +
                "httpMethod=" + httpMethod +
                ", url='" + url + '\'' +
                ", body=" + body +
                '}';
    }
}

package com.jitterted.yacht.adapter.out;

import com.jitterted.yacht.adapter.OutputTracker;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class JsonHttpClientTest {

    @Test
    void nulledGetThrowsNotFoundExceptionForUnconfiguredEndpoints() {
        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull();

        assertThatThrownBy(() -> {
            jsonHttpClient.get("/unconfigured", ExampleDto.class);
        }).isInstanceOf(NoSuchElementException.class)
          .hasMessage("URL not configured: /unconfigured");
    }

    @Test
    void nulledGetReturnsSingleConfiguredInstanceForever() {
        ExampleDto configuredExampleDto = new ExampleDto("configured value");

        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull(
                Map.of("/configured", configuredExampleDto)
        );

        ExampleDto exampleDto1 = jsonHttpClient.get("/configured", ExampleDto.class);
        assertThat(exampleDto1.getContent())
                .isEqualTo("configured value");

        ExampleDto exampleDto2 = jsonHttpClient.get("/configured", ExampleDto.class);
        assertThat(exampleDto2.getContent())
                .isEqualTo("configured value");

        ExampleDto exampleDto3 = jsonHttpClient.get("/configured", ExampleDto.class);
        assertThat(exampleDto3.getContent())
                .isEqualTo("configured value");
    }

    @Test
    void nullGetReturnsDifferentConfiguredInstancesWhenGivenList() {
        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull(
                Map.of("/configured-list", List.of(
                        new ExampleDto("dto 1"),
                        new ExampleDto("dto 2"),
                        new ExampleDto("dto 3"))));


        ExampleDto exampleDto1 = jsonHttpClient.get("/configured-list",
                                                    ExampleDto.class);
        assertThat(exampleDto1.getContent())
                .isEqualTo("dto 1");

        ExampleDto exampleDto2 = jsonHttpClient.get("/configured-list",
                                                    ExampleDto.class);
        assertThat(exampleDto2.getContent())
                .isEqualTo("dto 2");

        ExampleDto exampleDto3 = jsonHttpClient.get("/configured-list",
                                                    ExampleDto.class);
        assertThat(exampleDto3.getContent())
                .isEqualTo("dto 3");
    }

    @Test
    void nullGetThrowsExceptionWhenListOfConfiguredInstancesRunsOut() {
        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull(
                Map.of("/list-of-one?a=b", List.of(new ExampleDto("dto 1")))
        );
        jsonHttpClient.get("/list-of-one?a={parm}", ExampleDto.class, "b");

        assertThatThrownBy(() -> jsonHttpClient.get("/list-of-one?a={parm}", ExampleDto.class, "b"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No more responses configured for URL: /list-of-one?a=b");
    }

    @Test
    void nulledGetReturnsDifferentValuesBasedOnUriAndParameters() {
        ExampleDto configuredExampleDto1a = new ExampleDto("configured 1a");
        ExampleDto configuredExampleDto1b = new ExampleDto("configured 1b");
        ExampleDto configuredExampleDto2 = new ExampleDto("configured 2");

        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull(Map.of(
                "/configured1?parm=a", configuredExampleDto1a,
                "/configured1?parm=b", configuredExampleDto1b,
                "/configured2", configuredExampleDto2)
        );

        ExampleDto exampleDto1a = jsonHttpClient.get("/configured1?parm={first}",
                                                     ExampleDto.class,
                                                     "a");
        assertThat(exampleDto1a.getContent())
                .isEqualTo("configured 1a");

        ExampleDto exampleDto1b = jsonHttpClient.get("/configured1?parm={first}",
                                                     ExampleDto.class,
                                                     "b");
        assertThat(exampleDto1b.getContent())
                .isEqualTo("configured 1b");

        ExampleDto exampleDto2 = jsonHttpClient.get("/configured2", ExampleDto.class);
        assertThat(exampleDto2.getContent())
                .isEqualTo("configured 2");
    }


    public static class ExampleDto {
        private String content;

        public ExampleDto() {
            content = "initial value";
        }

        public ExampleDto(String configuredValue) {
            content = configuredValue;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    @Test
    void requestsAreTracked() {
        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull(
                Map.of("/get-endpoint?a", new ExampleDto())
        );

        OutputTracker<JsonHttpRequest> tracker = jsonHttpClient.trackRequests();

        jsonHttpClient.get("/get-endpoint?{parm}", ExampleDto.class, "a");

        ExampleDto postedBody = new ExampleDto("post");
        jsonHttpClient.post("/post-endpoint", postedBody);

        assertThat(tracker.output())
                .containsExactly(
                        JsonHttpRequest.createGet("/get-endpoint?a"),
                        JsonHttpRequest.createPost("/post-endpoint", postedBody));
    }


}
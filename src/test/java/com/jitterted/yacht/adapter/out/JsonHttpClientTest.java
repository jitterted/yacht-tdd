package com.jitterted.yacht.adapter.out;

import org.junit.jupiter.api.Test;

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
    void nulledGetReturnsConfiguredInstanceForever() {
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

    @Test
    public void nulledGetReturnsDifferentValuesForDifferentEndpoints() throws Exception {
        // url (incl. query params) -> Something, List<Something>
        // Something -> Exception, Object to Return
//         JsonHttpClient.createNull(Map.of("/first-url",

    }

//    @Test
//    void whenConfiguredNullFetcherThrowsExceptionIfCategoryHasNoConfiguredValue() {
//        AverageScoreFetcher fetcher =
//                AverageScoreFetcher.createNull(
//                        Map.of(ScoreCategory.FOURS, 1.0)
//                );
//
//        assertThatThrownBy(() -> {
//            fetcher.averageFor(ScoreCategory.FULLHOUSE);
//        }).isInstanceOf(NoSuchElementException.class)
//          .hasMessage("No average configured for FULLHOUSE in Null AverageScoreFetcher.");
//    }

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

}
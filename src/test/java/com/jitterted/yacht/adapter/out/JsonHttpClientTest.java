package com.jitterted.yacht.adapter.out;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JsonHttpClientTest {

    @Test
    void nulledGetReturnsUnconfiguredInstanceForever() {
        JsonHttpClient jsonHttpClient = JsonHttpClient.createNull();

        ExampleDto exampleDto1 = jsonHttpClient.get("/doesnotmatter",
                                                   ExampleDto.class);

        assertThat(exampleDto1.getContent())
                .isEqualTo("initial value");

        ExampleDto exampleDto2 = jsonHttpClient.get("/doesnotmatter",
                                                   ExampleDto.class);
        assertThat(exampleDto2.getContent())
                .isEqualTo("initial value");

        ExampleDto exampleDto3 = jsonHttpClient.get("/doesnotmatter",
                                                   ExampleDto.class);
        assertThat(exampleDto3.getContent())
                .isEqualTo("initial value");
    }

    public static class ExampleDto {
        private String content = "initial value";

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

//    @Test
//    void nullFetcherReturnsConfiguredValue() {
//        AverageScoreFetcher fetcher =
//                AverageScoreFetcher.createNull(
//                        Map.of(ScoreCategory.FOURS, 1.0,
//                               ScoreCategory.FULLHOUSE, 2.0,
//                               ScoreCategory.BIGSTRAIGHT, 3.0)
//                );
//
//        assertThat(fetcher.averageFor(ScoreCategory.FOURS))
//                .isEqualTo(1.0);
//    }
//
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

}
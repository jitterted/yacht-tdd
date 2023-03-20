package com.jitterted.yacht;

import com.jitterted.yacht.application.GameService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootTest
@Tag("spring")
class YachtApplicationTests {

    @Test
    void contextLoads() {
    }

    @TestConfiguration
    public static class Configuration {
        @Bean
        @Primary
        GameService createGameService() {
            return GameService.createNull();
        }
    }

}

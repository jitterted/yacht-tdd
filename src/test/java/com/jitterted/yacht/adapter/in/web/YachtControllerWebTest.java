package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.application.GameService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Disabled
class YachtControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void gameCorruptedExceptionReturns500() throws Exception {
        mockMvc.perform(post("/start-game"))
               .andExpect(status().is5xxServerError());
    }

    @TestConfiguration
    public static class Configuration {
        @Bean
        @Primary
        GameService createGameService() {
            return GameService.createNull(new GameService.NulledResponses()
                                                  .withCorruptedGame());
        }
    }


}
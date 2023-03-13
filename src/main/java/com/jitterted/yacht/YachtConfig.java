package com.jitterted.yacht;

import com.jitterted.yacht.application.GameService;
import org.springframework.context.annotation.Bean;

public class YachtConfig {

    @Bean
    public GameService createGameService() {
        return GameService.create();
    }
}

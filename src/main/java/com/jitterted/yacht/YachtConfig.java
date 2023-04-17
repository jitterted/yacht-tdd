package com.jitterted.yacht;

import com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase;
import com.jitterted.yacht.adapter.out.gamedatabase.GameDatabaseJpa;
import com.jitterted.yacht.application.GameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YachtConfig {

    // Needed for manual testing until we get rid of DeleteMeImpl
//    @Bean
//    public GameService createGameService() {
//        return GameService.create(new DeleteMeImpl());
//    }

    @Bean
    public GameService createGameService(GameDatabaseJpa gameDatabaseJpa) {
        return GameService.create(new GameDatabase(gameDatabaseJpa));
    }
}

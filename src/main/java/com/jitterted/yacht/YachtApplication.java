package com.jitterted.yacht;

import com.jitterted.yacht.adapter.out.dieroller.RandomDieRoller;
import com.jitterted.yacht.application.DiceRoller;
import com.jitterted.yacht.application.GameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class YachtApplication {

    public static void main(String[] args) {
        SpringApplication.run(YachtApplication.class, args);
    }

    @Bean
    public GameService createGameService() {
        RandomDieRoller dieRoller = RandomDieRoller.create();
        return new GameService(new DiceRoller(dieRoller));
    }

}

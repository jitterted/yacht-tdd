package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.HandOfDice;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameServiceDieRollerTest {

    @Test
    void rollDiceRollsAllDice() {
        GameService gameService = createGameServiceWithDieRollsOf(5, 1, 4, 2, 3);
        gameService.start();

        gameService.rollDice();

        assertThat(gameService.currentHand())
                .isEqualTo(HandOfDice.of(5, 1, 4, 2, 3));
    }

    @Test
    void reRollingDiceKeepsSpecifiedDice() {
        GameService gameService = createGameServiceWithDieRollsOf(6, 6, 6, 6, 6, 2, 3);
        gameService.start();
        gameService.rollDice();

        gameService.reRoll(List.of(6, 6, 6));

        assertThat(gameService.currentHand())
                .isEqualTo(HandOfDice.of(6, 6, 6, 2, 3));
    }

    private static GameService createGameServiceWithDieRollsOf(Integer... dieRolls) {
        return new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(dieRolls), 
                new InMemoryGameRepository());
    }

}
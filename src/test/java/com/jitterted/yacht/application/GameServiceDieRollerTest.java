package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.application.port.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.HandOfDice;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameServiceDieRollerTest {

    private static final ScoreCategoryNotifier NO_OP_SCORE_CATEGORY_NOTIFIER = (diceRoll, score, scoreCategory) -> {
    };

    @Test
    void rollDiceRollsAllDice() {
        GameService gameService = createGameServiceWithDieRollsOf(5, 1, 4, 2, 3);
        gameService.start();

        gameService.rollDice();

        assertThat(gameService.lastRoll())
                .isEqualTo(HandOfDice.of(5, 1, 4, 2, 3));
    }

    @Test
    void reRollingDiceKeepsSpecifiedDice() {
        GameService gameService = createGameServiceWithDieRollsOf(6, 6, 6, 6, 6, 2, 3);
        gameService.start();
        gameService.rollDice();

        gameService.reRoll(List.of(6, 6, 6));

        assertThat(gameService.lastRoll())
                .isEqualTo(HandOfDice.of(6, 6, 6, 2, 3));
    }

    private static GameService createGameServiceWithDieRollsOf(Integer... dies) {
        DieRoller dieRoller = DieRoller.createNull(dies);
        return new GameService(
                NO_OP_SCORE_CATEGORY_NOTIFIER,
                new AverageScoreFetcherStub(),
                dieRoller);
    }

}
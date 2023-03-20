package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.Scoreboard;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameServiceDieRollerTest {

    @Test
    void rollDiceRollsAllDice() throws Exception {
        GameService gameService = new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(5, 1, 4, 2, 3),
                GameDatabase.createNull());
        gameService.start();

        gameService.rollDice();

        assertThat(gameService.currentHand())
                .isEqualTo(HandOfDice.of(5, 1, 4, 2, 3));
    }

    @Test
    void reRollingDiceKeepsSpecifiedDice() throws Exception {
        GameService gameService = new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(2, 3),
                GameDatabase.createNull(
                        new Game.Snapshot(
                                1,
                                false,
                                HandOfDice.of(6, 6, 6, 6, 6),
                                new Scoreboard.Snapshot(Collections.emptyMap())
                        )
                ));

        Game game = gameService.reRoll(List.of(6, 6, 6));

        assertThat(game.currentHand())
                .isEqualTo(HandOfDice.of(6, 6, 6, 2, 3));
    }

}
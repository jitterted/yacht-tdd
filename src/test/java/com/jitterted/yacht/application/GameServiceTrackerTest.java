package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.Scoreboard;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameServiceTrackerTest {

    @Test
    void startSavesGame() throws Exception {
        GameService gameService = GameService.createNull();
        OutputTracker<Game> saveTracker = gameService.trackSaves();

        gameService.start();

        assertThat(saveTracker.output())
                .containsExactly(new Game());
    }

    @Test
    void rollDiceSavesGame() throws Exception {
        GameService gameService = new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(5, 4, 3, 2, 1),
                GameDatabase.createNull()
        );
        OutputTracker<Game> saveTracker = gameService.trackSaves();

        gameService.rollDice();

        Game expectedGame = new Game();
        expectedGame.diceRolled(HandOfDice.of(5, 4, 3, 2, 1));

        assertThat(saveTracker.output())
                .containsExactly(expectedGame);
    }

    @Test
    void reRollSavesGame() throws Exception {
        Game.Snapshot configuredSnapshot = new Game.Snapshot(
                1,
                false,
                HandOfDice.of(5, 4, 3, 2, 1),
                new Scoreboard.Snapshot(Collections.emptyMap())
        );
        GameService gameService = new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(6, 6, 6),
                GameDatabase.createNull(configuredSnapshot));
        OutputTracker<Game> saveTracker = gameService.trackSaves();

        List<Integer> keptDice = List.of(5, 4);
        gameService.reRoll(keptDice);

        Game expectedGame = Game.from(configuredSnapshot);
        expectedGame.diceReRolled(HandOfDice.of(5, 4, 6, 6, 6));

        assertThat(saveTracker.output())
                .containsExactly(expectedGame);
    }

    @Test
    void assignCurrentHandToScoreCategorySavesGame() throws Exception {
        Game.Snapshot configuredSnapshot = new Game.Snapshot(
                1,
                false,
                HandOfDice.of(5, 4, 3, 2, 1),
                new Scoreboard.Snapshot(Collections.emptyMap())
        );
        GameService gameService = new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(),
                GameDatabase.createNull(configuredSnapshot));
        OutputTracker<Game> saveTracker = gameService.trackSaves();

        gameService.assignCurrentHandTo(ScoreCategory.LITTLESTRAIGHT);

        Game expectedGame = Game.from(configuredSnapshot);
        expectedGame.assignCurrentHandTo(ScoreCategory.LITTLESTRAIGHT);

        assertThat(saveTracker.output())
                .containsExactly(expectedGame);
    }

}
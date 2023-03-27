package com.jitterted.yacht.application;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.out.averagescore.AverageScoreFetcher;
import com.jitterted.yacht.adapter.out.dieroller.DieRoller;
import com.jitterted.yacht.adapter.out.gamedatabase.GameDatabase;
import com.jitterted.yacht.adapter.out.scorecategory.ScoreCategoryNotifier;
import com.jitterted.yacht.domain.GameEvent;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameServiceTrackerTest {

    @Test
    void tracksStart() throws Exception {
        Fixture fixture = createFixture();

        fixture.gameService.start();

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.Started());
    }

    @Test
    void rollDiceSavesGame() throws Exception {
        Fixture fixture = createFixture();

        fixture.gameService.rollDice();

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.DiceRolled());
    }

    @Test
    void reRollSavesGame() throws Exception {
        Fixture fixture = createFixture();

        fixture.gameService.reRoll(List.of(1, 2, 3));

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.DiceRerolled(1, 2, 3));
    }

    @Test
    void assignCurrentHandToScoreCategorySavesGame() throws Exception {
        Fixture fixture = createFixture();

        fixture.gameService.assignCurrentHandTo(ScoreCategory.LITTLESTRAIGHT);

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent
                        .CategoryAssigned(ScoreCategory.LITTLESTRAIGHT));
    }

    record Fixture(GameService gameService,
                   OutputTracker<GameEvent> tracker){ }

    private Fixture createFixture() {
        GameService gameService = new GameService(
                ScoreCategoryNotifier.createNull(),
                AverageScoreFetcher.createNull(),
                DieRoller.createNull(),
                GameDatabase.createNull());
        OutputTracker<GameEvent> tracker = gameService.trackEvents();
        return new Fixture(gameService, tracker);
    }

}
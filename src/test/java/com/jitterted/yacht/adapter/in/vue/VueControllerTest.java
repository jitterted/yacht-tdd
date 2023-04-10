package com.jitterted.yacht.adapter.in.vue;

import com.jitterted.yacht.adapter.OutputTracker;
import com.jitterted.yacht.adapter.in.web.ScoredCategoryView;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.GameEvent;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class VueControllerTest {

    @Test
    public void startsGame() throws Exception {
        Fixture fixture = createFixture();

        fixture.vueController.startGame();

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.Started());
    }

    @Test
    public void canGetCurrentHand() throws Exception {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(6, 5, 4, 3, 2));
        Fixture fixture = createFixture(game);

        DiceRollDto dto = fixture.vueController.currentHand();

        assertThat(dto.getRoll())
                .containsExactly(6, 5, 4, 3, 2);
    }

    @Test
    public void rollsDice() throws Exception {
        Fixture fixture = createFixture();

        fixture.vueController.rollDice();

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.DiceRolled());
    }

    @Test
    public void canGetScoredCategories() throws Exception {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(5, 5, 5, 5, 5));
        game.assignCurrentHandTo(ScoreCategory.FIVES);
        Fixture fixture = createFixture(game);

        ScoreCategoriesDto scoreCategoriesDto = fixture.vueController.scoringCategories();

        // Collaborator-based Isolation
        assertThat(scoreCategoriesDto.getTotalScore())
                .isEqualTo(game.score());
        List<ScoredCategoryView> expectedCategories = ScoredCategoryView.viewOf(game.scoredCategories(), Collections.emptyMap());
        assertThat(scoreCategoriesDto.getCategories())
                .isEqualTo(expectedCategories);
    }

    @Test
    public void canAssignCurrentHandToCategory() throws Exception {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(6, 5, 4, 3, 2));
        Fixture fixture = createFixture(game);

        fixture.vueController.assignRollToCategory(Map.of("category", "SIXES"));

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.CategoryAssigned(ScoreCategory.SIXES));
    }

    @Test
    public void reRollKeepsSpecifiedDice() throws Exception {
        Game game = new Game();
        game.diceRolled(HandOfDice.of(6, 5, 4, 3, 2));
        Fixture fixture = createFixture(game);

        Keep keep = new Keep();
        keep.setDiceIndexesToKeep(List.of(0, 1, 3));

        fixture.vueController.reroll(keep);

        assertThat(fixture.tracker.output())
                .containsExactly(new GameEvent.DiceRerolled(6, 5, 3));
    }

    record Fixture(VueController vueController,
                   GameService gameService,
                   OutputTracker<GameEvent> tracker) {
    }

    private Fixture createFixture() {
        return createFixture(new Game());
    }

    private Fixture createFixture(Game game) {
        GameService gameService = GameService.createNull(
                new GameService.NulledResponses().withGame(game));
        OutputTracker<GameEvent> tracker = gameService.trackEvents();
        VueController vueController = new VueController(gameService);
        return new Fixture(vueController, gameService, tracker);
    }

}
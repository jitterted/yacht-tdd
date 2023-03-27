package com.jitterted.yacht.adapter.in.vue;

import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.HandOfDice;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class VueControllerTest {

    @Test
    public void postToStartGameStartsGame() throws Exception {
        GameService gameService = GameService.createNull();
        VueController vueController = new VueController(gameService);

        vueController.startGame();

        assertThat(gameService.roundCompleted())
                .isTrue();
    }

    @Test
    public void canGetCurrentHand() throws Exception {
        GameService gameService = GameService.createNull();
        VueController vueController = new VueController(gameService);
        vueController.startGame();

        DiceRollDto dto = vueController.currentHand();

        assertThat(dto.getRoll())
                .isEmpty();
    }

    @Test
    public void canRollDice() throws Exception {
        GameService gameService = GameService.createNull(
                new GameService.NulledResponses()
                        .withDieRolls(2, 3, 4, 5, 6));
        VueController vueController = new VueController(gameService);
        vueController.startGame();

        vueController.rollDice();
        DiceRollDto dto = vueController.currentHand();

        assertThat(dto.getRoll())
                .isEqualTo(List.of(2, 3, 4, 5, 6));
    }

    @Test
    public void canGetScoredCategories() throws Exception {
        GameService gameService = GameService.createNull();
        VueController vueController = new VueController(gameService);
        vueController.startGame();

        ScoreCategoriesDto scoreCategoriesDto = vueController.scoringCategories();

        assertThat(scoreCategoriesDto.getTotalScore())
                .isZero();
        assertThat(scoreCategoriesDto.getCategories())
                .hasSize(ScoreCategory.values().length);
    }

    @Test
    public void canAssignCurrentHandToCategory() throws Exception {
        GameService gameService = GameService.createNull(
                new GameService.NulledResponses()
                        .withDieRolls(6, 6, 5, 5, 5));
        VueController vueController = new VueController(gameService);
        vueController.startGame();
        vueController.rollDice();

        vueController.assignRollToCategory(Map.of("category", "SIXES"));

        ScoreCategoriesDto scoreCategoriesDto = vueController.scoringCategories();
        assertThat(scoreCategoriesDto.getTotalScore())
                .isEqualTo(6 + 6);
    }

    @Test
    public void reRollKeepsSpecifiedDice() throws Exception {
        GameService gameService = GameService.createNull(
                new GameService.NulledResponses()
                        .withDieRolls(1, 2, 3, 4, 5, 6, 6));
        VueController vueController = new VueController(gameService);
        vueController.startGame();
        vueController.rollDice();

        Keep keep = new Keep();
        keep.setDiceIndexesToKeep(List.of(1, 2, 4));

        vueController.reroll(keep);

        assertThat(gameService.currentHand())
                .isEqualTo(HandOfDice.of(2, 3, 5, 6, 6));
    }

}
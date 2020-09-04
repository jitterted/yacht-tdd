package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.StubDiceRoller;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class VueControllerTest {

  @Test
  public void newControllerResultsInGameNotYetStarted() throws Exception {
    Game game = new Game();

    VueController vueController = new VueController(game);

    assertThat(game.roundCompleted())
        .isFalse();
  }

  @Test
  public void postToStartGameStartsGame() throws Exception {
    Game game = new Game();
    VueController vueController = new VueController(game);

    vueController.startGame();

    assertThat(game.roundCompleted())
        .isTrue();
  }

  @Test
  public void newGameStartedWhenGetLastRollReturnsEmptyDiceRoll() throws Exception {
    Game game = new Game();
    VueController vueController = new VueController(game);
    vueController.startGame();

    DiceRollDto dto = vueController.lastRoll();

    assertThat(dto.getRoll())
        .isEmpty();
  }

  @Test
  public void gameStartedRollDiceButtonRollsTheDice() throws Exception {
    Game game = new Game(new StubDiceRoller(DiceRoll.of(2, 3, 4, 5, 6)));
    VueController vueController = new VueController(game);
    vueController.startGame();

    vueController.rollDice();
    DiceRollDto dto = vueController.lastRoll();

    assertThat(dto.getRoll())
        .isEqualTo(List.of(2, 3, 4, 5, 6));
  }

  @Test
  public void scoreCategoriesReturnsScoredCategories() throws Exception {
    Game game = new Game();
    VueController vueController = new VueController(game);
    vueController.startGame();

    ScoreCategoriesDto scoreCategoriesDto = vueController.scoringCategories();

    assertThat(scoreCategoriesDto.getTotalScore())
        .isZero();
    assertThat(scoreCategoriesDto.getCategories())
        .hasSize(ScoreCategory.values().length);
  }

  @Test
  public void assignLastRollToCategoryThenCategoryIsAssignedAndScored() throws Exception {
    Game game = new Game(new StubDiceRoller(DiceRoll.of(6, 6, 5, 5, 5)));
    VueController vueController = new VueController(game);
    vueController.startGame();
    vueController.rollDice();

    vueController.assignRollToCategory(Map.of("category", "SIXES"));

    ScoreCategoriesDto scoreCategoriesDto = vueController.scoringCategories();
    assertThat(scoreCategoriesDto.getTotalScore())
        .isEqualTo(6 + 6);
  }

  @Test
  public void keepDiceReRollsTheNonKeptDiceUsingSpy() throws Exception {
    Game spyGame = spy(Game.class);
    VueController vueController = new VueController(spyGame);
    vueController.startGame();
    vueController.rollDice();

    Keep keep = new Keep();
    keep.setDiceIndexesToKeep(List.of(1, 2, 4));
    List<Integer> keptDiceValues = keep.diceValuesFrom(spyGame.lastRoll());

    vueController.reroll(keep);

    verify(spyGame).reRoll(keptDiceValues);
  }

}
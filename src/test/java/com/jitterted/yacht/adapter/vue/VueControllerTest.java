package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.StubDiceRoller;
import com.jitterted.yacht.domain.DiceRoll;
import com.jitterted.yacht.domain.Game;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        .isEqualTo("DiceRoll: []");
  }

  @Test
  public void gameStartedRollDiceButtonRollsTheDice() throws Exception {
    Game game = new Game(new StubDiceRoller(DiceRoll.of(2, 3, 4, 5, 6)));
    VueController vueController = new VueController(game);
    vueController.startGame();

    vueController.rollDice();
    DiceRollDto dto = vueController.lastRoll();

    assertThat(dto.getRoll())
        .isEqualTo("DiceRoll: [2, 3, 4, 5, 6]");
  }

}
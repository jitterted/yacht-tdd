package com.jitterted.yacht.adapter.vue;

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
        .isEqualTo("DiceRoll: [0, 0, 0, 0, 0]");
  }

}
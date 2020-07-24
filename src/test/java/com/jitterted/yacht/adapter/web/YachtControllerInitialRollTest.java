package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.StubDiceRoller;
import com.jitterted.yacht.domain.Game;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerInitialRollTest {

  @Test
  public void initialDiceRollReturnsRollInResultsPage() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(3, 1, 4, 1, 5));
    YachtController yachtController = new YachtController(game);

    yachtController.rollDice();
    Model model = new ConcurrentModel();
    yachtController.rollResult(model);

    assertThat(model.getAttribute("roll"))
        .isEqualTo(List.of(3, 1, 4, 1, 5));

    assertThat(model.getAttribute("score"))
        .isEqualTo("0");
  }

}
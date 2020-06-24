package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.StubDiceRoller;
import com.jitterted.yacht.domain.Game;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerTest {

  @Test
  public void rollingDiceReturnsRollInResultsPage() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(3, 1, 4, 1, 5));
    YachtController yachtController = new YachtController(game);

    yachtController.rollDice();
    Model model = new ConcurrentModel();
    yachtController.rollResult(model);

    assertThat(model.getAttribute("roll"))
        .isEqualTo("3 1 4 1 5");

    assertThat(model.containsAttribute("score"))
        .isTrue();
  }

  @Test
  public void assignDiceRoll13355ToThreesResultsInScoreOf6() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(1, 3, 3, 5, 5));
    YachtController yachtController = new YachtController(game);
    yachtController.rollDice();

    yachtController.assignRollToCategory("threes");

    assertThat(game.score())
        .isEqualTo(3 + 3);
  }

  @Test
  public void assignDiceRoll22244ToFullHouseResultsInScoreOf6() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(4, 4, 2, 2, 2));
    YachtController yachtController = new YachtController(game);
    yachtController.rollDice();

    yachtController.assignRollToCategory("fullhouse");

    assertThat(game.score())
        .isEqualTo(4 + 4 + 2 + 2 + 2);
  }

  // Another form of YAGNI: Don't Solve Problems That You Don't Yet Have (DSPTYDYH)

}
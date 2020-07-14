package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.StubDiceRoller;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class YachtControllerTest {

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

  @Test
  public void assignDiceRoll13355ToThreesResultsInScoreOf6() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(1, 3, 3, 5, 5));
    YachtController yachtController = new YachtController(game);
    yachtController.rollDice();

    yachtController.assignRollToCategory(ScoreCategory.THREES.toString());

    Model model = new ConcurrentModel();
    yachtController.rollResult(model);
    assertThat(model.getAttribute("score"))
        .isEqualTo(String.valueOf(3 + 3));
  }

  @Test
  public void assignDiceRoll22244ToFullHouseResultsInScoreOf6() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(4, 4, 2, 2, 2));
    YachtController yachtController = new YachtController(game);
    yachtController.rollDice();

    yachtController.assignRollToCategory(ScoreCategory.FULLHOUSE.toString());

    assertThat(game.score())
        .isEqualTo(4 + 4 + 2 + 2 + 2);
  }

  @Test
  public void assignDiceRoll11123ToOnesResultsInScoreOf3() throws Exception {
    Game game = new Game(StubDiceRoller.createDiceRollerFor(1, 1, 1, 2, 3));

    YachtController yachtController = new YachtController(game);
    yachtController.rollDice();

    yachtController.assignRollToCategory(ScoreCategory.ONES.toString());

    assertThat(game.score())
        .isEqualTo(1 + 1 + 1);
  }

  // Another form of YAGNI: Don't Solve Problems That You Don't Yet Have (DSPTYDYH)

}
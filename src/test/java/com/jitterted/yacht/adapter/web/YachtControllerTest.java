package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.StubDiceRoller;
import com.jitterted.yacht.domain.DiceRoller;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.StubDieRoller;
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
  // Another form of YAGNI: Don't Solve Problems That You Don't Yet Have (DSPTYDYH)

  @Test
  public void reRollGeneratesNewRollIncludingKeptDice() throws Exception {
    Game game = new Game(new DiceRoller(new StubDieRoller(List.of(3, 1, 4, 1, 5, 3, 2, 6))));
    YachtController yachtController = new YachtController(game);
    Keep keep = new Keep();
    keep.setDiceIndexesToKeep(List.of(1, 3, 4)); // 1, 1, 5

    yachtController.rollDice();
    yachtController.reRoll(keep);

    Model model = new ConcurrentModel();
    yachtController.rollResult(model);

    assertThat(model.getAttribute("roll"))
        .isEqualTo(List.of(1, 1, 5, 3, 2));
  }

}
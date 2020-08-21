package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VueController {
  private final Game game;

  @Autowired
  public VueController(Game game) {
    this.game = game;
  }

  @PostMapping("start-game")
  public void startGame() {
    game.start();
  }

  @GetMapping("last-roll")
  public DiceRollDto lastRoll() {
    return DiceRollDto.from(game.lastRoll());
  }
}

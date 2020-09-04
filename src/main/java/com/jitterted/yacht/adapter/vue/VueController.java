package com.jitterted.yacht.adapter.vue;

import com.jitterted.yacht.adapter.web.ScoredCategoryView;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.TooManyRollsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

  @PostMapping("roll-dice")
  public void rollDice() {
    game.rollDice();
  }

  @GetMapping("score-categories")
  public ScoreCategoriesDto scoringCategories() {
    return new ScoreCategoriesDto(game.score(),
                                  ScoredCategoryView.viewOf(game.scoredCategories()));
  }

  @PostMapping(value = "assign-roll", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void assignRollToCategory(@RequestBody Map<String, String> map) {
    String scoreCategoryString = map.get("category");
    ScoreCategory scoreCategory = ScoreCategory.valueOf(scoreCategoryString.toUpperCase());
    game.assignRollTo(scoreCategory);
  }

  @PostMapping(value = "reroll", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void reroll(@RequestBody Keep keep) {
    List<Integer> keptDice = keep.diceValuesFrom(game.lastRoll());
    game.reRoll(keptDice);
  }

  @ExceptionHandler(TooManyRollsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void onTooManyRollsException() {
  }
}

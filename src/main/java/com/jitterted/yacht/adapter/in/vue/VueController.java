package com.jitterted.yacht.adapter.in.vue;

import com.jitterted.yacht.adapter.in.web.ScoredCategoryView;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VueController {
    private final GameService gameService;

    @Autowired
    public VueController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("start-game")
    public void startGame() {
        gameService.start();
    }

    @GetMapping("last-roll")
    public DiceRollDto lastRoll() {
        return DiceRollDto.from(gameService.currentHand());
    }

    @PostMapping("roll-dice")
    public void rollDice() {
        gameService.rollDice();
    }

    @GetMapping("score-categories")
    public ScoreCategoriesDto scoringCategories() {
        return new ScoreCategoriesDto(gameService.score(),
                                      ScoredCategoryView.viewOf(
                                              gameService.scoredCategories(),
                                              Collections.emptyMap()));
    }

    @PostMapping(value = "assign-roll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void assignRollToCategory(@RequestBody Map<String, String> map) {
        String scoreCategoryString = map.get("category");
        ScoreCategory scoreCategory = ScoreCategory.valueOf(scoreCategoryString.toUpperCase());
        gameService.assignRollTo(scoreCategory);
    }

    @PostMapping(value = "reroll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void reroll(@RequestBody Keep keep) {
        List<Integer> keptDice = keep.diceValuesFrom(gameService.currentHand());
        gameService.reRoll(keptDice);
    }

    @ExceptionHandler(TooManyRollsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void onTooManyRollsException() {
    }
}

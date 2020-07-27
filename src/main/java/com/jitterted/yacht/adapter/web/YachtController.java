package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.Game;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class YachtController {

  private final Game game;

  @Autowired
  public YachtController(Game game) {
    this.game = game;
  }

  @PostMapping("/rolldice")
  public String rollDice() {
    game.rollDice();
    return "redirect:/rollresult";
  }

  @GetMapping("/rollresult")
  public String rollResult(Model model) {
    addCurrentScoreTo(model);
    model.addAttribute("roll", RollView.listOf(game.lastRoll()));
    addCategoriesTo(model);
    model.addAttribute("canReRoll", game.canReRoll());
    model.addAttribute("rollAssignedToCategory", game.lastRollAssignedToCategory());

    model.addAttribute("keep", new Keep());
    return "roll-result";
  }

  private void addCategoriesTo(Model model) {
    model.addAttribute("categories", viewOf(game.scoredCategories()));
  }

  @PostMapping("/re-roll")
  public String reRoll(Keep keep) {
    List<Integer> keptDice = keep.diceValuesFrom(game.lastRoll());
    game.reRoll(keptDice);
    return "redirect:/rollresult";
  }


  private List<ScoredCategoryView> viewOf(List<ScoredCategory> scoredCategories) {
    return scoredCategories.stream()
                           .sorted(Comparator.comparing(ScoredCategory::scoreCategory))
                           .map(ScoredCategoryView::from)
                           .collect(Collectors.toList());
  }

  @PostMapping("/select-category")
  public String assignRollToCategory(@RequestParam("category") String category) {
    ScoreCategory scoreCategory = ScoreCategory.valueOf(category.toUpperCase());
    game.assignRollTo(scoreCategory);

    if (game.allCategoriesAssigned()) {
      return "redirect:/game-over";
    }
    return "redirect:/rollresult";
  }

  @GetMapping("/game-over")
  public String displayGameOverPage(Model model) {
    addCurrentScoreTo(model);
    addCategoriesTo(model);
    return "game-over";
  }

  private void addCurrentScoreTo(Model model) {
    model.addAttribute("score", String.valueOf(game.score()));
  }
}

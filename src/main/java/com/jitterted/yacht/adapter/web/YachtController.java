package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.ScoreCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class YachtController {

    private final GameService gameService;

    @Autowired
    public YachtController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start-game")
    public String startGame() {
        gameService.start();
        return "redirect:/rollresult";
    }

    @PostMapping("/rolldice")
    public String rollDice() {
        gameService.rollDice();
        return "redirect:/rollresult";
    }

    @GetMapping("/rollresult")
    public String rollResult(Model model) {
        addCurrentScoreTo(model);
        model.addAttribute("roll", RollView.listOf(gameService.lastRoll()));
        addCategoriesTo(model);
        model.addAttribute("canReRoll", gameService.canReRoll());
        model.addAttribute("roundCompleted", gameService.roundCompleted());
        model.addAttribute("keep", new Keep());
        model.addAttribute("categoryNames", ScoreCategory.values());
        return "roll-result";
    }

    @PostMapping("/re-roll")
    public String reRoll(Keep keep) {
        List<Integer> keptDice = keep.diceValuesFrom(gameService.lastRoll());
        gameService.reRoll(keptDice);
        return "redirect:/rollresult";
    }


    @PostMapping("/select-category")
    public String assignRollToCategory(@RequestParam("category") String category) {
        ScoreCategory scoreCategory = ScoreCategory.valueOf(category.toUpperCase());
        gameService.assignRollTo(scoreCategory);

        if (gameService.isOver()) {
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

    private void addCategoriesTo(Model model) {
        model.addAttribute("categories", ScoredCategoryView.viewOf(gameService.scoredCategories()));
    }

    private void addCurrentScoreTo(Model model) {
        model.addAttribute("score", String.valueOf(gameService.score()));
    }
}

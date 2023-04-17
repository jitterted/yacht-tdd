package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.adapter.out.gamedatabase.GameCorrupted;
import com.jitterted.yacht.application.GameService;
import com.jitterted.yacht.application.Keep;
import com.jitterted.yacht.domain.ScoreCategory;
import com.jitterted.yacht.domain.ScoredCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String rollDice() throws GameCorrupted {
        gameService.rollDice();
        return "redirect:/rollresult";
    }

    @GetMapping("/rollresult")
    public String rollResult(Model model) throws GameCorrupted {
        // CONTINUE: Make decision here whether to show Roll Result or Game Over
        model.addAttribute("score", gameService.score());
        model.addAttribute("roll", RollView.listOf(gameService.currentHand()));
        addCategoriesTo(model);
        model.addAttribute("canReRoll", gameService.canReRoll());
        model.addAttribute("roundCompleted", gameService.roundCompleted());
        model.addAttribute("keep", new Keep());
        model.addAttribute("categoryNames", ScoreCategory.values());
        return "roll-result";
    }

    @PostMapping("/re-roll")
    public String reRoll(Keep keep) throws GameCorrupted {
        List<Integer> keptDice = keep.diceValuesFrom(gameService.currentHand());
        gameService.reRoll(keptDice);
        return "redirect:/rollresult";
    }


    @PostMapping("/select-category")
    public String assignCurrentHandToCategory(@RequestParam("category") String category) throws GameCorrupted {
        ScoreCategory scoreCategory = ScoreCategory.valueOf(category.toUpperCase());
        gameService.assignCurrentHandTo(scoreCategory);

        if (gameService.isOver()) {
            return "redirect:/game-over";
        }
        return "redirect:/rollresult";
    }

    @GetMapping("/game-over")
    public String displayGameOverPage(Model model) throws GameCorrupted {
        model.addAttribute("score", gameService.score());
        addCategoriesTo(model);
        return "game-over";
    }

    private void addCategoriesTo(Model model) throws GameCorrupted {
        List<ScoredCategory> scoredCategories = gameService.scoredCategories();
        List<ScoreCategory> scoreCategories =
                scoredCategories.stream()
                                .map(ScoredCategory::scoreCategory)
                                .collect(Collectors.toList());
        Map<ScoreCategory, Double> averages = gameService.averagesFor(scoreCategories);
        model.addAttribute("categories",
                           ScoredCategoryView.viewOf(
                                   scoredCategories,
                                   averages));
    }

}

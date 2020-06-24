package com.jitterted.yacht.adapter.web;

import com.jitterted.yacht.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class YachtController {

  private final Game game;

  @Autowired
  public YachtController(Game game) {
    this.game = game;
  }

  @PostMapping("/rolldice")
  public String rollDice() {
    return "redirect:/rollresult";
  }

  @GetMapping("/rollresult")
  public String rollResult(Model model) {
    model.addAttribute("score", "0");
    model.addAttribute("roll", "12345");
    return "roll-result.html";
  }
}

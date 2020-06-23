package com.jitterted.yacht.adapter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class YachtController {

  @PostMapping("/rolldice")
  public String rollDice() {
    return "redirect:/rollresult";
  }
}

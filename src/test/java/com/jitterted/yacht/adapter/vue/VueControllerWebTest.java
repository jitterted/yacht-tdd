package com.jitterted.yacht.adapter.vue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@Tag("spring")
public class VueControllerWebTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void postToStartGameStartsTheGame() throws Exception {
    mockMvc.perform(post("/api/start-game"))
           .andExpect(status().is2xxSuccessful());
    mockMvc.perform(get("/api/last-roll"))
           .andExpect(status().is2xxSuccessful())
           .andExpect(jsonPath("$.roll").value("DiceRoll: []"));
  }

  @Test
  public void postToRollDiceSucceeds() throws Exception {
    mockMvc.perform(post("/api/roll-dice"))
           .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void requestForScoreCategoriesReturnsCategories() throws Exception {
    mockMvc.perform(get("/api/score-categories"))
           .andExpect(status().is2xxSuccessful())
           .andExpect(jsonPath("$.categories").isArray())
           .andExpect(jsonPath("$.totalScore").isNotEmpty());
  }
}

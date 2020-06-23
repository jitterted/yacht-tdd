package com.jitterted.yacht.adapter.web;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Tag("spring")
public class WebTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getOfHomePageIsStatus200() throws Exception {
    mockMvc.perform(get("/index.html"))
           .andExpect(status().is2xxSuccessful())
           .andExpect(content()
                          .string(containsStringIgnoringCase("<button>roll dice</button>"))
           );
  }

  @Test
  public void postToRollDice() throws Exception {
    mockMvc.perform(post("/rolldice"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/rollresult"));
  }

}

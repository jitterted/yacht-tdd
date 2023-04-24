package com.jitterted.yacht.adapter.in.web;

import com.jitterted.yacht.application.GameService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Tag("spring")
public class WebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GameService gameService;

    @Test
    public void getOfHomePageIsStatus200() throws Exception {
        mockMvc.perform(get("/index.html"))
               .andExpect(status().is2xxSuccessful()
               );
    }

    @Test
    public void postToRollDiceRedirectsToRollResult() throws Exception {
        mockMvc.perform(post("/rolldice"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/game"));
    }

    @Test
    public void postToSelectCategoryScoresRollResultForThatCategory() throws Exception {
        mockMvc.perform(post("/select-category")
                                .param("category", "threes"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/game"));
    }

}

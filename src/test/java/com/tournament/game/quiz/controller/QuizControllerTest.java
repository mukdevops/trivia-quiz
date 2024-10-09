package com.tournament.game.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tournament.game.quiz.constants.TriviaConstants;
import com.tournament.game.quiz.model.TriviaAnswer;
import com.tournament.game.quiz.service.TriviaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class QuizControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TriviaService triviaService;

    @InjectMocks
    private QuizController quizController;


    @Test
    void validateAnswer_ShouldReturn200() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();

        // Mock the service layer
        Mockito.when(triviaService.validateAnswer(1L, "Test")).thenReturn(TriviaConstants.STATUS_SUCCESS);
        TriviaAnswer triviaAnswer = new TriviaAnswer();
        triviaAnswer.setAnswer("Test");

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(triviaAnswer)))
                .andExpect(status().isOk());

    }

    @Test
    void validateAnswer_ShouldReturn400() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();

         // Mock the service layer
        Mockito.when(triviaService.validateAnswer(1L, "Correct")).thenReturn(TriviaConstants.STATUS_FAILURE);
        TriviaAnswer triviaAnswer = new TriviaAnswer();
        triviaAnswer.setAnswer("Incorrect");

        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(triviaAnswer)))
                .andExpect(status().isBadRequest());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


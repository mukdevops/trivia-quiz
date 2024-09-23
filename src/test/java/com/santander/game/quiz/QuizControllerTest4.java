package com.santander.game.quiz;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.game.quiz.controller.QuizController;
import com.santander.game.quiz.model.TriviaAnswer;
import com.santander.game.quiz.model.TriviaQuestion;
import com.santander.game.quiz.service.TriviaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuizController.class)
class QuizControllerTest4 {

  @Autowired private MockMvc mockMvc;

  @MockBean private TriviaService triviaService;

  @Test
  void startTrivia_ShouldReturn200() throws Exception {

    //  mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
    // Mock the service layer
    Mockito.when(triviaService.startTrivia()).thenReturn(new TriviaQuestion());

    mockMvc
        .perform(post("/trivia/start").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void validateAnswer_ShouldReturn200() throws Exception {

    //  mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();

    //        TriviaEntity triviaEntity = new TriviaEntity();
    //        triviaEntity.setCorrectAnswer("Test");
    //
    //        Optional<TriviaEntity> triviaEntityOptional = Optional.of(triviaEntity);
    //
    //        Mockito.when(triviaRepository.findById(1L)).thenReturn(triviaEntityOptional);

    //   given(triviaService.validateAnswer(1L, "Test")).willReturn("STATUS_SUCCESS");
    //  given().(triviaService.validateAnswer(1L,"Test"))

    // Mock the service layer
    Mockito.when(triviaService.validateAnswer(1l, "Test")).thenReturn("STATUS_SUCCESS");
    final TriviaAnswer triviaAnswer = new TriviaAnswer();
    triviaAnswer.setAnswer("Test");
    mockMvc
        .perform(
            put("/trivia/reply/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(triviaAnswer)))
        .andExpect(status().isOk());
  }

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }
}

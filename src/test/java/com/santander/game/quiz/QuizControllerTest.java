package com.santander.game.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.game.quiz.controller.QuizController;
import com.santander.game.quiz.entity.TriviaEntity;
import com.santander.game.quiz.model.TriviaAnswer;
import com.santander.game.quiz.model.TriviaQuestion;
import com.santander.game.quiz.repository.TriviaRepository;
import com.santander.game.quiz.service.TriviaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.awaitility.Awaitility.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void startTrivia_ShouldReturn200() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();
        // Mock the service layer
        Mockito.when(triviaService.startTrivia()).thenReturn(new TriviaQuestion());

        mockMvc.perform(post("/trivia/start")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void validateAnswer_ShouldReturn200() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();

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
        TriviaAnswer triviaAnswer = new TriviaAnswer();
        triviaAnswer.setAnswer("Test");
        mockMvc.perform(put("/trivia/reply/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(triviaAnswer)))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


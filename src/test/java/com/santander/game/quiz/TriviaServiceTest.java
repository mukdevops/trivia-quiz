package com.santander.game.quiz;


import com.santander.game.quiz.entity.TriviaEntity;
import com.santander.game.quiz.entity.TriviaQuestionEntity;
import com.santander.game.quiz.model.TriviaApiResponse;
import com.santander.game.quiz.model.TriviaQuestion;
import com.santander.game.quiz.repository.TriviaQuestionRepository;
import com.santander.game.quiz.repository.TriviaRepository;
import com.santander.game.quiz.service.TriviaService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TriviaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TriviaQuestionRepository triviaQuestionRepository;

    @Mock
    private TriviaRepository triviaRepository;

    @InjectMocks
    private TriviaService triviaService;

    @Test
    void startTrivia_ShouldCallOpenTriviaAPI_Dummy_Test() {
        // Mock the trivia API response
        TriviaApiResponse mockResponse = getStubbedTriviaApiResponse();
        when(restTemplate.getForObject(anyString(), eq(TriviaApiResponse.class)))
                .thenReturn(mockResponse);

        TriviaQuestionEntity triviaQuestionEntity = new TriviaQuestionEntity();
        triviaQuestionEntity.setId(1L);
        when(triviaQuestionRepository.save(triviaQuestionEntity)).thenReturn(triviaQuestionEntity);
        triviaService.startTrivia();

        // Verify that the trivia API was called
        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }

    @Test
    void startTrivia_ShouldCallOpenTriviaAPI() {
        // Mock the trivia API response
        TriviaApiResponse mockResponse = getStubbedTriviaApiResponse();
        when(restTemplate.getForObject(anyString(), eq(TriviaApiResponse.class)))
                .thenReturn(mockResponse);

        TriviaEntity triviaQuestionEntity = new TriviaEntity();
        triviaQuestionEntity.setTriviaId(1L);
        when(triviaRepository.save(triviaQuestionEntity)).thenReturn(triviaQuestionEntity);
        triviaService.startTrivia();

        // Verify that the trivia API was called
        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }

    private static TriviaApiResponse getStubbedTriviaApiResponse() {
        TriviaApiResponse mockResponse = new TriviaApiResponse();
        TriviaQuestion triviaQuestion = new TriviaQuestion();
        triviaQuestion.setQuestion("What is the capital of Fiji?");
        triviaQuestion.setCorrectAnswer("Sua");
        mockResponse.setResults(List.of(triviaQuestion));
        return mockResponse;
    }
}


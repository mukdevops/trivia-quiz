package com.tournament.game.quiz.service;


import com.tournament.game.quiz.entity.TriviaEntity;
import com.tournament.game.quiz.entity.TriviaQuestionEntity;
import com.tournament.game.quiz.exception.OpenAPIConnectionException;
import com.tournament.game.quiz.model.TriviaApiResponse;
import com.tournament.game.quiz.model.TriviaQuestion;
import com.tournament.game.quiz.repository.TriviaQuestionRepository;
import com.tournament.game.quiz.repository.TriviaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tournament.game.quiz.constants.TriviaConstants.STATUS_FAILURE;
import static com.tournament.game.quiz.constants.TriviaConstants.STATUS_MAX_ATTEMPTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TriviaServiceTest {

    @Mock
    private RestTemplate restTemplate;

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

        TriviaEntity triviaEntity = new TriviaEntity();
        triviaEntity.setTriviaId(123L);
        when(triviaRepository.save(any(TriviaEntity.class))).thenReturn(triviaEntity);
        triviaService.returnQuestionWithPossibleAnswers();

        // Verify that the trivia API was called
        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }

    @Test
    void validateCorrectAnswer_ShouldCheckDatabaseForAnswer() {
        // Mock the trivia API response
        TriviaEntity triviaEntity = getStubbedTriviaEntity();
        when(triviaRepository.findById(anyLong())).thenReturn(Optional.of(triviaEntity));
        triviaService.validateAnswer(123L,"Sua");

        // Verify that the trivia API was called
        verify(triviaRepository, times(1)).deleteById(anyLong());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 2 })
    void validateIncorrectAnswerForThreeAttempts_ShouldCheckDatabaseForAnswer(Long attemptNumber) {
        // Mock the trivia API response
        TriviaEntity triviaEntity = getStubbedTriviaEntity();

        triviaEntity.setAnswerAttempts(attemptNumber);

        when(triviaRepository.findById(anyLong())).thenReturn(Optional.of(triviaEntity));

        String response = triviaService.validateAnswer(123L,"London");
        // Verify that the trivia API was called
        verify(triviaRepository, times(1)).incrementAnswerAttempts(anyLong(), anyLong());
        assertEquals(STATUS_FAILURE, response);
    }

    @Test
    void validateIncorrectAnswerOnFourthAttempt_ShouldCheckDatabaseForAnswer() {
        // Mock the trivia API response
        TriviaEntity triviaEntity = getStubbedTriviaEntity();
        triviaEntity.setAnswerAttempts(3L);
        when(triviaRepository.findById(anyLong())).thenReturn(Optional.of(triviaEntity));
        triviaService.validateAnswer(123L,"London");

        // Verify that the trivia API was called
        String response = triviaService.validateAnswer(123L,"London");
        // Verify that the trivia API was called
        assertEquals(STATUS_MAX_ATTEMPTS, response);
    }

    @Test
    void startTrivia_ShouldThrowOpenAPIConnectionExceptionWhenResultsIsNull() {
        // Mock the trivia API response
        TriviaApiResponse mockResponse = getStubbedTriviaApiResponse();
        mockResponse.setResults(null);
        when(restTemplate.getForObject(anyString(), eq(TriviaApiResponse.class)))
                .thenReturn(mockResponse);

        assertThrows(OpenAPIConnectionException.class, () -> triviaService.returnQuestionWithPossibleAnswers());

        // Verify that the trivia API was called
        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }

    @Test
    void startTrivia_ShouldThrowOpenAPIConnectionExceptionWhenAPIResponseIsNull() {

        when(restTemplate.getForObject(anyString(), eq(TriviaApiResponse.class)))
                .thenReturn(null);

        assertThrows(OpenAPIConnectionException.class, () -> triviaService.returnQuestionWithPossibleAnswers());

        // Verify that the trivia API was called
        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }

    private static TriviaApiResponse getStubbedTriviaApiResponse() {
        TriviaApiResponse mockResponse = new TriviaApiResponse();
        TriviaQuestion triviaQuestion = new TriviaQuestion();
        triviaQuestion.setQuestion("What is the capital of Fiji?");
        triviaQuestion.setCorrectAnswer("Sua");
        triviaQuestion.setIncorrectAnswers(Arrays.asList("Newyork","London"));
        mockResponse.setResults(List.of(triviaQuestion));
        return mockResponse;
    }

    private static TriviaEntity getStubbedTriviaEntity() {
        TriviaEntity triviaEntity = new TriviaEntity();
        triviaEntity.setTriviaId(123L);
        triviaEntity.setCorrectAnswer("Sua");
        return triviaEntity;
    }
}


package com.santander.game.quiz.service;

import static com.santander.game.quiz.constants.TriviaConstants.*;

import com.santander.game.quiz.entity.TriviaEntity;
import com.santander.game.quiz.entity.TriviaQuestionEntity;
import com.santander.game.quiz.mapper.TriviaQuestionMapper;
import com.santander.game.quiz.model.Trivia;
import com.santander.game.quiz.model.TriviaApiResponse;
import com.santander.game.quiz.model.TriviaQuestion;
import com.santander.game.quiz.model.TriviaResponse;
import com.santander.game.quiz.repository.TriviaQuestionRepository;
import com.santander.game.quiz.repository.TriviaRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class TriviaService {

  @Autowired private RestTemplate restTemplate;

  private final TriviaQuestionRepository triviaQuestionRepository;

  private final TriviaRepository triviaRepository;

  public TriviaService(
      final RestTemplate restTemplate,
      final TriviaQuestionRepository triviaQuestionRepository,
      final TriviaRepository triviaRepository) {
    this.restTemplate = restTemplate;
    this.triviaQuestionRepository = triviaQuestionRepository;
    this.triviaRepository = triviaRepository;
  }

  @Transactional
  public TriviaQuestion startTrivia() {
    // Call Open Trivia DB API
    final TriviaApiResponse apiResponse =
        restTemplate.getForObject(TRIVIA_API_URL, TriviaApiResponse.class);

    if (apiResponse != null && apiResponse.getResults() != null) {
      final TriviaQuestion triviaQuestion = apiResponse.getResults().get(0);
      final TriviaQuestionEntity triviaQuestionEntity =
          TriviaQuestionMapper.modelToEntity(triviaQuestion);
      triviaQuestionRepository.save(triviaQuestionEntity);
      return triviaQuestion;
    } else {
      throw new RuntimeException("Failed to fetch trivia questions from API");
    }
  }

  public List<TriviaQuestionEntity> getAllTriviaQuestions() {
    return triviaQuestionRepository.findAll();
  }

  public TriviaQuestionEntity saveTriviaQuestion(final TriviaQuestionEntity triviaQuestion) {
    return triviaQuestionRepository.save(triviaQuestion);
  }

  @Transactional
  public TriviaResponse returnQuestionWithPossibleAnswers() {
    // Call Open Trivia DB API
    final TriviaApiResponse apiResponse =
        restTemplate.getForObject(TRIVIA_API_URL, TriviaApiResponse.class);

    if (apiResponse != null && apiResponse.getResults() != null) {

      TriviaEntity triviaEntity =
          TriviaQuestionMapper.modelToTriviaEntity(apiResponse.getResults().get(0));

      triviaEntity = triviaRepository.save(triviaEntity);

      final Trivia trivia = getTrivia(triviaEntity, apiResponse);

      return TriviaResponse.builder()
          .status(HttpStatus.OK.getReasonPhrase())
          .trivia(trivia)
          .build();
    } else {
      throw new RuntimeException("Failed to fetch trivia questions from API");
    }
  }

  @Transactional
  public String validateAnswer(final Long triviaId, final String answer) {

    final Optional<TriviaEntity> triviaEntityOptional = triviaRepository.findById(triviaId);

    if (triviaEntityOptional.isPresent()) {
      final TriviaEntity triviaEntity = triviaEntityOptional.get();
      return checkAnswer(answer, triviaEntity);
    } else {
      return STATUS_INVALID_QUESTION;
    }
  }

  private String checkAnswer(final String answer, final TriviaEntity triviaEntity) {
    if (triviaEntity.getAnswerAttempts() > 2) {
      // check if max attempts reached
      return STATUS_MAX_ATTEMPTS;
    } else if (triviaEntity.getCorrectAnswer().equalsIgnoreCase(answer)) {
      // correct answer
      triviaRepository.deleteById(triviaEntity.getTriviaId());
      return STATUS_SUCCESS;
    } else {
      // increase the attempts by 1
      triviaRepository.incrementAnswerAttempts(
          triviaEntity.getTriviaId(), triviaEntity.getAnswerAttempts() + 1L);
      return STATUS_FAILURE;
    }
  }

  private static Trivia getTrivia(
      final TriviaEntity triviaEntity, final TriviaApiResponse apiResponse) {
    return Trivia.builder()
        .triviaId(triviaEntity.getTriviaId())
        .question(triviaEntity.getQuestion())
        .possibleAnswers(getPossibleAnswers(apiResponse))
        .build();
  }

  private static List<String> getPossibleAnswers(final TriviaApiResponse apiResponse) {
    final List<String> possibleAnswers = new ArrayList<>();
    possibleAnswers.add(apiResponse.getResults().get(0).getCorrectAnswer());
    possibleAnswers.addAll(apiResponse.getResults().get(0).getIncorrectAnswers());
    return possibleAnswers;
  }
}

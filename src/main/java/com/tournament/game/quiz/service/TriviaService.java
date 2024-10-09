package com.tournament.game.quiz.service;

import static com.tournament.game.quiz.constants.TriviaConstants.*;

import com.tournament.game.quiz.entity.TriviaEntity;
import com.tournament.game.quiz.exception.OpenAPIConnectionException;
import com.tournament.game.quiz.mapper.TriviaQuestionMapper;
import com.tournament.game.quiz.model.Trivia;
import com.tournament.game.quiz.model.TriviaApiResponse;
import com.tournament.game.quiz.model.TriviaResponse;
import com.tournament.game.quiz.repository.TriviaRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class TriviaService {

  private final RestTemplate restTemplate;

  private final TriviaRepository triviaRepository;

  public TriviaService(
      final RestTemplate restTemplate,
      final TriviaRepository triviaRepository) {
    this.restTemplate = restTemplate;
    this.triviaRepository = triviaRepository;
  }

  @Transactional
  public TriviaResponse returnQuestionWithPossibleAnswers() {
    // Call Open Trivia API
    final TriviaApiResponse apiResponse =
        restTemplate.getForObject(TRIVIA_API_URL, TriviaApiResponse.class);

    if (apiResponse != null && apiResponse.getResults() != null) {

      // Mapping from Open API Response to Database entity
      TriviaEntity triviaEntity =
          TriviaQuestionMapper.modelToTriviaEntity(apiResponse.getResults().get(0));

      // Question details storing in database
      triviaEntity = triviaRepository.save(triviaEntity);

      return TriviaResponse.builder()
          .status(HttpStatus.OK.getReasonPhrase())
          .trivia(getTrivia(triviaEntity, apiResponse))
          .build();
    } else {
      throw new OpenAPIConnectionException("Failed to fetch trivia questions from API");
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

    if (triviaEntity.getCorrectAnswer().equalsIgnoreCase(answer)) {
      // correct answer
      triviaRepository.deleteById(triviaEntity.getTriviaId());
      return STATUS_SUCCESS;
    } else if (triviaEntity.getAnswerAttempts() != null && triviaEntity.getAnswerAttempts() > 2) {
      // check if max attempts reached
      return STATUS_MAX_ATTEMPTS;
    } else {
      // increase the attempts by 1
      triviaRepository.incrementAnswerAttempts(
          triviaEntity.getTriviaId(),
          Optional.ofNullable(triviaEntity.getAnswerAttempts()).orElse(0L) + 1L);
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

package com.tournament.game.quiz.controller;

import com.tournament.game.quiz.exception.QuestionNotFoundException;
import com.tournament.game.quiz.model.TriviaAnswer;
import com.tournament.game.quiz.model.TriviaResponse;
import com.tournament.game.quiz.service.TriviaService;
import com.tournament.game.quiz.constants.TriviaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600,
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RequestMapping("/trivia")
public class QuizController {

    private final TriviaService triviaService;

    public QuizController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @PostMapping("/start")
    public ResponseEntity<TriviaResponse> returnQuestion() {

        TriviaResponse triviaResponse = triviaService.returnQuestionWithPossibleAnswers();

        return ResponseEntity.ok(triviaResponse);
    }

    @PutMapping("/reply/{triviaId}")
    public ResponseEntity<String> acceptAnswer(@PathVariable long triviaId,
                                       @RequestBody TriviaAnswer triviaAnswer) {

        String triviaResponse = triviaService.validateAnswer(triviaId,triviaAnswer.getAnswer());

        if(triviaResponse.equals(TriviaConstants.STATUS_INVALID_QUESTION)){
            throw new QuestionNotFoundException("No such question!");
        }

        return mapHttpResponse(triviaResponse);
    }

    private ResponseEntity<String> mapHttpResponse(String triviaResponse){

        HttpStatusCode httpStatusCode = switch (triviaResponse) {
            case TriviaConstants.STATUS_SUCCESS -> HttpStatus.OK;
            case TriviaConstants.STATUS_FAILURE -> HttpStatus.BAD_REQUEST;
            case TriviaConstants.STATUS_MAX_ATTEMPTS -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.NOT_FOUND;
        };
        return ResponseEntity.status(httpStatusCode).body(triviaResponse);
    }
}

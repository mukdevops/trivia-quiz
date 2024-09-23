package com.santander.game.quiz.controller;

import com.santander.game.quiz.model.TriviaAnswer;
import com.santander.game.quiz.model.TriviaQuestion;
import com.santander.game.quiz.model.TriviaResponse;
import com.santander.game.quiz.service.TriviaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.santander.game.quiz.constants.TriviaConstants.*;

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

    @GetMapping("/start-test")
    public ResponseEntity<TriviaQuestion> startTrivia() {
        TriviaQuestion triviaQuestion = triviaService.startTrivia();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return ResponseEntity.ok(triviaQuestion);
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

        return mapHttpResponse(triviaResponse);
    }

    private ResponseEntity<String> mapHttpResponse(String triviaResponse){

        HttpStatusCode httpStatusCode = switch (triviaResponse) {
            case STATUS_SUCCESS -> HttpStatus.OK;
            case STATUS_FAILURE -> HttpStatus.BAD_REQUEST;
            case STATUS_MAX_ATTEMPTS -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.NOT_FOUND;
        };
        return ResponseEntity.status(httpStatusCode).body(triviaResponse);
    }
}

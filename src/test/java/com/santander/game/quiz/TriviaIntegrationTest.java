package com.santander.game.quiz;

import com.santander.game.quiz.model.TriviaQuestion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TriviaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void startTriviaIntegrationTest() {
        ResponseEntity<TriviaQuestion> response = restTemplate.postForEntity("/trivia/start", null, TriviaQuestion.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}


package com.santander.game.quiz.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TriviaResponse implements Serializable {

    private String status;

    private Trivia trivia;
}

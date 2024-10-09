package com.tournament.game.quiz.model;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Trivia implements Serializable {

    private Long triviaId;

    private String question;

    private List<String> possibleAnswers;
}

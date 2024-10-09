package com.tournament.game.quiz.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TriviaApiResponse implements Serializable {

    private int responseCode;

    private List<TriviaQuestion> results;
}

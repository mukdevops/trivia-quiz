package com.tournament.game.quiz.mapper;

import com.tournament.game.quiz.entity.TriviaEntity;
import com.tournament.game.quiz.model.TriviaQuestion;

public class TriviaQuestionMapper {

    private TriviaQuestionMapper() {
    }

    public static TriviaEntity modelToTriviaEntity(TriviaQuestion triviaQuestion) {
        TriviaEntity entity = new TriviaEntity();
        entity.setQuestion(triviaQuestion.getQuestion());
        entity.setCorrectAnswer(triviaQuestion.getCorrectAnswer());
        return entity;
    }
}


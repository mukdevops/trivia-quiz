package com.santander.game.quiz.mapper;

import com.santander.game.quiz.entity.TriviaEntity;
import com.santander.game.quiz.entity.TriviaQuestionEntity;
import com.santander.game.quiz.model.TriviaQuestion;

public class TriviaQuestionMapper {

    private TriviaQuestionMapper() {
    }

    // Map from Entity to DTO
    public static TriviaQuestion entityToModel(TriviaQuestionEntity entity) {
        TriviaQuestion triviaQuestionModel = new TriviaQuestion();
        triviaQuestionModel.setCategory(entity.getCategory());
        triviaQuestionModel.setQuestion(entity.getQuestion());
        triviaQuestionModel.setCorrectAnswer(entity.getCorrectAnswer());
        triviaQuestionModel.setDifficulty(entity.getDifficulty());
        triviaQuestionModel.setIncorrectAnswers(entity.getIncorrectAnswers());
        return triviaQuestionModel;
    }

    // Map from DTO to Entity
    public static TriviaQuestionEntity modelToEntity(TriviaQuestion triviaQuestion) {
        TriviaQuestionEntity entity = new TriviaQuestionEntity();
        entity.setCategory(triviaQuestion.getCategory());
        entity.setQuestion(triviaQuestion.getQuestion());
        entity.setCorrectAnswer(triviaQuestion.getCorrectAnswer());
        entity.setDifficulty(triviaQuestion.getDifficulty());
        entity.setIncorrectAnswers(triviaQuestion.getIncorrectAnswers());
        return entity;
    }

    public static TriviaEntity modelToTriviaEntity(TriviaQuestion triviaQuestion) {
        TriviaEntity entity = new TriviaEntity();
        entity.setQuestion(triviaQuestion.getQuestion());
        entity.setCorrectAnswer(triviaQuestion.getCorrectAnswer());
        return entity;
    }
}


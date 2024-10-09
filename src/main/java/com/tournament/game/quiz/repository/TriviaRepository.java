package com.tournament.game.quiz.repository;

import com.tournament.game.quiz.entity.TriviaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TriviaRepository extends JpaRepository<TriviaEntity, Long> {

    @Modifying
    @Query("UPDATE TriviaEntity triviaEntity SET triviaEntity.answerAttempts = :numberOfFailedAttempts" +
            " WHERE triviaEntity.id = :triviaId")
    int incrementAnswerAttempts(Long triviaId, Long numberOfFailedAttempts);
}


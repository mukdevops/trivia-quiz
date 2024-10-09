package com.tournament.game.quiz.repository;

import com.tournament.game.quiz.entity.TriviaQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriviaQuestionRepository extends JpaRepository<TriviaQuestionEntity, Long> {
}


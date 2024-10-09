package com.tournament.game.quiz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trivia")
@Getter
@Setter
public class TriviaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trivia_id_generator")
    @SequenceGenerator( name = "trivia_id_generator", sequenceName = "trivia_id_seq", allocationSize = 1)
    @Column(name = "trivia_id")
    private Long triviaId;

    @Column(name = "question")
    private String question;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "answer_attempts")
    private Long answerAttempts;
}

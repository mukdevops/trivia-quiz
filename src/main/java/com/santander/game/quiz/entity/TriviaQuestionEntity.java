package com.santander.game.quiz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "trivia_question")
@Getter
@Setter
public class TriviaQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trivia_question_id_generator")
    @SequenceGenerator( name = "trivia_question_id_generator", sequenceName = "trivia_question_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "category")
    private String category;

    @Column(name = "question")
    private String question;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "difficulty")
    private String difficulty;

    @Column(name = "incorrect_answers")
    private List<String> incorrectAnswers;
}

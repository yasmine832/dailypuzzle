package com.example.dailypuzzle.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Puzzle {

    public enum PuzzleType {
        RIDDLE,
        MATH_PROBLEM,
        TRIVIA,
        LOGIC_PUZZLE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String title;
    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Enumerated(EnumType.STRING)
    private PuzzleType type;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    //comments
    //private String difficulty; // e.g., "EASY", "MEDIUM", "HARD"
    //private String source;// api source puzzle

    private boolean isSolved = false;

    //private User user; //many to one uniek?


    public Puzzle() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public PuzzleType getType() {
        return type;
    }

    public void setType(PuzzleType type) {
        this.type = type;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setSolved(boolean solved) {
        isSolved = solved;
    }
}

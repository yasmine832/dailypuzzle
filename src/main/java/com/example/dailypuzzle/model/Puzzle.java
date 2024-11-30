package com.example.dailypuzzle.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Puzzle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String title;
    @Column(nullable = false)
    private String answer;// string??
    @Column(nullable = false)
    private String question;    //string?
    @Column(nullable = false)
    private String category; //enum DIFFUCTLIES  /:levels
    //private String source; / @column(nullable = false, name=)

    private LocalDateTime expirationDate; //is 24H

    //private User user; //many to one uniek?


    //@manyToMany(mappedBy = "")
    // private list User usersSolved


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}

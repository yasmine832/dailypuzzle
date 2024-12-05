package com.example.dailypuzzle.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Puzzle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String title;
    private String answer;// string??
    private String question;    //string?
    private String type; //enum?

    //comment

    //comment

    //comment

    //comment

    //comment

    //comment

    private LocalDateTime expirationDate; //is 24H

    //private User user; //many to one uniek?

    //getters en setters



}

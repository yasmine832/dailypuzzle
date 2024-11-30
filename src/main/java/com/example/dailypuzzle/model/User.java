package com.example.dailypuzzle.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // db is responsible for generating id
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(name="chosen time", nullable = false)
    private String chosenTime;

  //foreign key
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //removed from list, removed from db
    private List<SolvedPuzzle> solvedPuzzles;

    //user cans olve multiple puzzles and this entity tracks progress

    public List<SolvedPuzzle> getSolvedPuzzles() {
        return solvedPuzzles;
    }

    public void setSolvedPuzzles(List<SolvedPuzzle> solvedPuzzles) {
        this.solvedPuzzles = solvedPuzzles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChosenTime() {
        return chosenTime;
    }

    public void setChosenTime(String chosenTime) {
        this.chosenTime = chosenTime;
    }
}

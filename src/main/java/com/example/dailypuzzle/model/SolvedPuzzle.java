package com.example.dailypuzzle.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SolvedPuzzle { //joined table

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "puzzle_id", nullable = false)
    private Puzzle puzzle;

    @Column(nullable = false)
    private LocalDateTime solvedDate;


    private int totalSolved;
    private double averageSolveTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public LocalDateTime getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(LocalDateTime solvedDate) {
        this.solvedDate = solvedDate;
    }

    public int getTotalSolved() {
        return totalSolved;
    }

    public void setTotalSolved(int totalSolved) {
        this.totalSolved = totalSolved;
    }

    public double getAverageSolveTime() {
        return averageSolveTime;
    }

    public void setAverageSolveTime(double averageSolveTime) {
        this.averageSolveTime = averageSolveTime;
    }
}

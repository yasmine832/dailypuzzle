package com.example.dailypuzzle.repository;


import com.example.dailypuzzle.model.Puzzle;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {
    Puzzle findByQuestion(String question);


    List<Puzzle> findByExpirationDateAfter(LocalDateTime expirationDate);
}

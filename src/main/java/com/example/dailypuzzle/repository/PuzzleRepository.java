package com.example.dailypuzzle.repository;


import com.example.dailypuzzle.model.Puzzle;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

@SuppressWarnings("JpaMissingIdInspection")
@Entity
public interface PuzzleRepository extends JpaRepository<Puzzle, Long> {
    //Puzzle findByQuestion(String question);







}

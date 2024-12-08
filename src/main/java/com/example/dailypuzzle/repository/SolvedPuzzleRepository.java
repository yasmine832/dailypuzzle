package com.example.dailypuzzle.repository;


import com.example.dailypuzzle.model.Puzzle;
import com.example.dailypuzzle.model.SolvedPuzzle;
import com.example.dailypuzzle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolvedPuzzleRepository extends JpaRepository<SolvedPuzzle, Long> {
    List<SolvedPuzzle> findByUserId(Long userId);

    boolean existsByUserAndPuzzle(User user, Puzzle puzzle);
}

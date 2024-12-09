package com.example.dailypuzzle.repository;


import com.example.dailypuzzle.model.Puzzle;
import com.example.dailypuzzle.model.SolvedPuzzle;
import com.example.dailypuzzle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolvedPuzzleRepository extends JpaRepository<SolvedPuzzle, Long> {
    List<SolvedPuzzle> findByUserId(Long userId);

    boolean existsByUserAndPuzzle(User user, Puzzle puzzle);

    int countByUser(User user);

    List<SolvedPuzzle> findByUser(User user);

    List<SolvedPuzzle> findByUserOrderBySolvedDateDesc(User user);

    @Query(value = "SELECT AVG(TIMESTAMPDIFF(SECOND, p.created_date, s.solved_date)) " +
            "FROM solved_puzzle s " +
            "JOIN puzzle p ON s.puzzle_id = p.id " +
            "WHERE s.user_id = :userId", nativeQuery = true)
    Double calculateAverageSolveTime(@Param("userId") Long userId);



}

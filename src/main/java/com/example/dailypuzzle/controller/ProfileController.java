package com.example.dailypuzzle.controller;


import com.example.dailypuzzle.model.Puzzle;
import com.example.dailypuzzle.model.SolvedPuzzle;
import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.SolvedPuzzleRepository;
import com.example.dailypuzzle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private SolvedPuzzleRepository solvedPuzzleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String viewProfile(ModelMap model, Principal principal) {

        // Get current logged-in user
        User currentUser = userRepository.findByUsername(principal.getName());

        int totalSolved = solvedPuzzleRepository.countByUser(currentUser);
        Double averageSolveTime = solvedPuzzleRepository.calculateAverageSolveTime(currentUser.getId());
        if (averageSolveTime == null) {
            averageSolveTime = 0.0; // Use a default value if no data is available
        }
        List<SolvedPuzzle> solvedPuzzles = solvedPuzzleRepository.findByUser(currentUser);

        model.addAttribute("user", currentUser);
        model.addAttribute("totalSolved", totalSolved);
        model.addAttribute("averageSolveTime", averageSolveTime);
        model.addAttribute("solvedPuzzles", solvedPuzzles);

        return "profile/profile-page";
    }

    private double calculateAverageSolveTime(List<SolvedPuzzle> solvedPuzzles) {
        if (solvedPuzzles.isEmpty()) return 0;

        return solvedPuzzles.stream()
                .mapToLong(sp -> Duration.between(sp.getPuzzle().getCreatedDate(), sp.getSolvedDate()).toMinutes())
                .average()
                .orElse(0);
    }

    private int calculateSolveStreak(List<SolvedPuzzle> solvedPuzzles) {
        // logic to calculate consecutive days of solving puzzles
        // simplified
        return (int) solvedPuzzles.stream()
                .map(sp -> sp.getSolvedDate().toLocalDate())
                .distinct()
                .count();
    }

    private Map<Puzzle.PuzzleType, Long> calculatePuzzleTypeBreakdown(List<SolvedPuzzle> solvedPuzzles) {
        return solvedPuzzles.stream()
                .map(SolvedPuzzle::getPuzzle)
                .collect(Collectors.groupingBy(
                        Puzzle::getType,
                        Collectors.counting()
                ));
    }
}



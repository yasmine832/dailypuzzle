package com.example.dailypuzzle.controller;

import ch.qos.logback.core.model.Model;
import com.example.dailypuzzle.model.Puzzle;
import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.UserRepository;
import com.example.dailypuzzle.service.PuzzleService;
import com.example.dailypuzzle.service.TriviaPuzzleService;
import com.example.dailypuzzle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/puzzles")
public class PuzzleController {


    @Autowired
    private PuzzleService puzzleService;

    @Autowired
    private TriviaPuzzleService triviaPuzzleService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showDailyPuzzles(ModelMap model, Principal principal) {
        // Get current logged-in user
        User currentUser = userRepository.findByUsername(principal.getName());

        // Get active puzzles for user
        List<Puzzle> activePuzzles = puzzleService.getActivePuzzlesForUser(currentUser);

        model.addAttribute("puzzles", activePuzzles);
        return "puzzles/daily-puzzles";
    }


    @PostMapping("/solve/{puzzleId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String solvePuzzle(
            @PathVariable Long puzzleId,
            @RequestParam String answer,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User currentUser = userRepository.findByUsername(principal.getName());

            boolean isCorrect = puzzleService.solvePuzzle(currentUser, puzzleId, answer);

            if (isCorrect) {
                redirectAttributes.addFlashAttribute("successMessage", "Congratulations! Puzzle solved correctly.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Incorrect answer. Try again!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/puzzles";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/fetch")
    public String manualFetchPuzzles(ModelMap model, Principal principal) {
        // Fetch puzzles
        List<Puzzle> fetchedPuzzles = puzzleService.fetchDailyPuzzles();

        // Get current user
        User currentUser = userRepository.findByUsername(principal.getName());

        // Fetch active puzzles for user (which now includes newly fetched puzzles)
        List<Puzzle> activePuzzles = puzzleService.getActivePuzzlesForUser(currentUser);

        model.addAttribute("puzzles", activePuzzles);
        model.addAttribute("fetchMessage", "Puzzles manually fetched: " + fetchedPuzzles.size() + " puzzles");

        return "puzzles/daily-puzzles";
    }

    // endpoint to specifically test Trivia Puzzle fetching
    @GetMapping("/fetch-trivia")
    public String testTriviaPuzzle(ModelMap model) {
        Puzzle triviaPuzzle = triviaPuzzleService.fetchTriviaPuzzle();

        model.addAttribute("triviaPuzzle", triviaPuzzle);
        return "puzzles/trivia-test";
    }
}
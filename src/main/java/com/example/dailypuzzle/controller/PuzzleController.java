package com.example.dailypuzzle.controller;

import ch.qos.logback.core.model.Model;
import com.example.dailypuzzle.model.Puzzle;
import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.UserRepository;
import com.example.dailypuzzle.service.PuzzleService;
import com.example.dailypuzzle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/puzzles")
public class PuzzleController {


    @Autowired
    private PuzzleService puzzleService;

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
}








}

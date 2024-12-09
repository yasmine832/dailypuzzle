package com.example.dailypuzzle.service;

import com.example.dailypuzzle.model.Puzzle;
import com.example.dailypuzzle.model.SolvedPuzzle;
import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.PuzzleRepository;
import com.example.dailypuzzle.repository.SolvedPuzzleRepository;
import com.example.dailypuzzle.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PuzzleService {


    @Autowired
    private PuzzleRepository puzzleRepository;

    @Autowired
    private SolvedPuzzleRepository solvedPuzzleRepository;

    @Autowired
    private UserRepository userRepository;


    private static final String API_URL_SUDOKU = "https://sudoku-api.vercel.app/api/dosuku";
    private static final String API_URL_CROSSWORD = "";
    private static final String API_URL_WORDSEARCH = "";


    private static final Logger logger = LoggerFactory.getLogger(PuzzleService.class);


    @Autowired
    private TriviaPuzzleService triviaPuzzleService;

    public List<Puzzle> fetchDailyPuzzles() {
        try {
            List<Puzzle> puzzles = new ArrayList<>();

            // Fetch Trivia Puzzle
            Puzzle triviaPuzzle = triviaPuzzleService.fetchTriviaPuzzle();
            if (triviaPuzzle != null) {
                triviaPuzzle.setExpirationDate(LocalDateTime.now().plusHours(24));
                puzzles.add(triviaPuzzle);
            }

            // Add a backup puzzle if trivia fails
            if (puzzles.isEmpty()) {
                puzzles = generateSamplePuzzles();
            }

            // Save puzzles and ensure correctAnswer is never null
            return puzzleRepository.saveAll(puzzles.stream()
                    .filter(p -> p.getCorrectAnswer() != null && !p.getCorrectAnswer().isEmpty())
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error("Error fetching daily puzzles", e);
            return generateSamplePuzzles();
        }
    }

    public void fetchAndDeliverPuzzles(User user) {
        try {
            List<Puzzle> puzzles = fetchDailyPuzzles();

            // Associate puzzles with the user if needed
            puzzles.forEach(puzzle -> {
                // Optional: Additional logic for user-specific puzzle assignment
                logger.info("Puzzle delivered: {} to user {}", puzzle.getQuestion(), user.getUsername());
            });

            // Update user's puzzle stats
            updateUserPuzzleStats(user, puzzles);

            logger.info("Successfully delivered puzzles to user: {}", user.getUsername());
        } catch (Exception e) {
            logger.error("Failed to deliver puzzles to user: {}", user.getUsername(), e);
        }
    }

    private void updateUserPuzzleStats(User user, List<Puzzle> puzzles) {
        // method to update user's puzzle-related statistics
        // track total puzzles delivered, etc.
    }


    public List<Puzzle> getActivePuzzlesForUser(User user) {
        List<Puzzle> activePuzzles = puzzleRepository.findByExpirationDateAfter(LocalDateTime.now());

        return activePuzzles.stream().map(p -> {
            // Check if puzzle is already solved by user
            boolean isSolved = solvedPuzzleRepository.existsByUserAndPuzzle(user, p);
            p.setSolved(isSolved);
            return p;
        }).collect(Collectors.toList());
    }

    // Method to check and save solved puzzle
    public boolean solvePuzzle(User user, Long puzzleId, String userAnswer) throws Exception {
        Puzzle puzzle = puzzleRepository.findById(puzzleId)
                .orElseThrow(() -> new Exception("Puzzle not found"));

        // Check if puzzle is expired
        if (LocalDateTime.now().isAfter(puzzle.getExpirationDate())) {
            throw new IllegalStateException("Puzzle has expired");
        }

        // Check if the user has already solved this puzzle
        if (solvedPuzzleRepository.existsByUserAndPuzzle(user, puzzle)) {
            throw new IllegalStateException("You have already solved this puzzle");
        }


        // Check if answer is correct (case-insensitive)
        if (puzzle.getCorrectAnswer() != null &&
                puzzle.getCorrectAnswer().trim().equalsIgnoreCase(userAnswer.trim())) {
            // Create and save solved puzzle
            SolvedPuzzle solvedPuzzle = new SolvedPuzzle();
            solvedPuzzle.setUser(user);
            solvedPuzzle.setPuzzle(puzzle);
            solvedPuzzle.setSolvedDate(LocalDateTime.now());

            solvedPuzzleRepository.save(solvedPuzzle);
            return true;
        }

        return false;
    }

    // Fallback method to generate sample puzzles
    private List<Puzzle> generateSamplePuzzles() {
        List<Puzzle> samplePuzzles = new ArrayList<>();

        Puzzle puzzle1 = new Puzzle();
        puzzle1.setQuestion("What has keys, but no locks; space, but no room; and you can enter, but not go in?");
        puzzle1.setCorrectAnswer("A keyboard");
        puzzle1.setAnswer(null);
        puzzle1.setType(Puzzle.PuzzleType.RIDDLE);
        puzzle1.setExpirationDate(LocalDateTime.now().plusHours(24));

        Puzzle puzzle2 = new Puzzle();
        puzzle2.setQuestion("If you have me, you want to share me. If you share me, you haven't got me. What am I?");
        puzzle2.setCorrectAnswer("A secret");
        puzzle2.setAnswer(null);
        puzzle2.setType(Puzzle.PuzzleType.RIDDLE);
        puzzle2.setExpirationDate(LocalDateTime.now().plusHours(24));

        samplePuzzles.add(puzzle1);
        samplePuzzles.add(puzzle2);

        return puzzleRepository.saveAll(samplePuzzles);
    }

}



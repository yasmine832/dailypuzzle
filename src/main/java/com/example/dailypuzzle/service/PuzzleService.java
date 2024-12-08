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


    // External API integration method (example with RestTemplate)
    public List<Puzzle> fetchDailyPuzzles() {
        try {
            // Example API call - replace with actual API integration
            RestTemplate restTemplate = new RestTemplate();
            // Fetch 2 puzzles from an external API
            ResponseEntity<List<Puzzle>> response = restTemplate.exchange(
                    "https://api.example.com/puzzles", //make genral so user can pick
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Puzzle>>() {
                    }
            );

            List<Puzzle> puzzles = response.getBody();

            // Set expiration date to 24 hours from now
            puzzles.forEach(puzzle -> {
                puzzle.setExpirationDate(LocalDateTime.now().plusHours(24));
            });

            return puzzleRepository.saveAll(puzzles);
        } catch (Exception e) {
            logger.error("Error fetching daily puzzles", e);
            // Fallback: Generate sample puzzles if API fails
            return generateSamplePuzzles();
        }
    }

    // Method to get active puzzles for a user
    public List<Puzzle> getActivePuzzlesForUser(User user) {
        List<Puzzle> activePuzzles = puzzleRepository.findByExpirationDateAfter(LocalDateTime.now());

        return activePuzzles.stream().map(p -> {
            Puzzle puzzle = new Puzzle();
            puzzle.setId(p.getId());
            puzzle.setQuestion(p.getQuestion());
            puzzle.setType(p.getType());//get name TODO
            puzzle.setExpirationDate(p.getExpirationDate());

            // Check if puzzle is already solved by user
            boolean isSolved = solvedPuzzleRepository.existsByUserAndPuzzle(user, puzzle);
            puzzle.setSolved(isSolved);

            return puzzle;
        }).collect(Collectors.toList());
    }

    // Method to save puzzles
    public List<Puzzle> saveDailyPuzzles(List<Puzzle> puzzles) {
        return puzzleRepository.saveAll(puzzles);
    }


    // Method to check and save solved puzzle
    public boolean solvePuzzle(User user, Long puzzleId, String userAnswer) throws Exception {
        Puzzle puzzle = puzzleRepository.findById(puzzleId)
                .orElseThrow(() -> new Exception("Puzzle not found"));

        // Check if puzzle is expired
        if (LocalDateTime.now().isAfter(puzzle.getExpirationDate())) {
            throw new IllegalStateException("Puzzle has expired");
        }

        // Check if answer is correct (case-insensitive)
        if (puzzle.getAnswer().trim().equalsIgnoreCase(userAnswer.trim())) {
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
        puzzle1.setAnswer("A keyboard");
        puzzle1.setType(Puzzle.PuzzleType.RIDDLE);
        puzzle1.setExpirationDate(LocalDateTime.now().plusHours(24));

        Puzzle puzzle2 = new Puzzle();
        puzzle2.setQuestion("If you have me, you want to share me. If you share me, you haven't got me. What am I?");
        puzzle2.setAnswer("A secret");
        puzzle2.setType(Puzzle.PuzzleType.RIDDLE);
        puzzle2.setExpirationDate(LocalDateTime.now().plusHours(24));

        samplePuzzles.add(puzzle1);
        samplePuzzles.add(puzzle2);

        return puzzleRepository.saveAll(samplePuzzles);
    }

}



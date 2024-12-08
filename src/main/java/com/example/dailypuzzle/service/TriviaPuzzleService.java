package com.example.dailypuzzle.service;

import com.example.dailypuzzle.model.Puzzle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class TriviaPuzzleService {
    private static final Logger logger = LoggerFactory.getLogger(TriviaPuzzleService.class);
    private static final String TRIVIA_API_URL = "https://opentdb.com/api.php?amount=1&type=multiple";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Puzzle fetchTriviaPuzzle() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(TRIVIA_API_URL, String.class);

            // Parse the JSON response
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode resultsArray = rootNode.get("results");

            if (resultsArray.isArray() && resultsArray.size() > 0) {
                JsonNode triviaQuestion = resultsArray.get(0);

                Puzzle puzzle = new Puzzle();
                puzzle.setQuestion(decodeHtmlEntities(triviaQuestion.get("question").asText()));
                puzzle.setCorrectAnswer(decodeHtmlEntities(triviaQuestion.get("correct_answer").asText()));
                puzzle.setAnswer(null); // User has to provide the answer
                puzzle.setType(Puzzle.PuzzleType.TRIVIA);

                return puzzle;
            }
        } catch (Exception e) {
            logger.error("Error fetching trivia puzzle", e);
        }
        return null;
    }

    private String decodeHtmlEntities(String input) {
        return input.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">");
    }
}
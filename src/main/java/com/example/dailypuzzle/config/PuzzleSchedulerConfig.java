package com.example.dailypuzzle.config;

import com.example.dailypuzzle.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class PuzzleSchedulerConfig {

    @Autowired
    private PuzzleService puzzleService;

    @Scheduled(cron = "0 0 0 * * ?") // midnight for now
    public void dailyPuzzleRefresh() {
        // Fetch and save new daily puzzles
        puzzleService.fetchDailyPuzzles();
    }
}
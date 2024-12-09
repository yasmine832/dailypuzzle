package com.example.dailypuzzle.config;

import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.UserRepository;
import com.example.dailypuzzle.service.PuzzleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@EnableScheduling
public class PuzzleSchedulerConfig {

    @Autowired
    private PuzzleService puzzleService;
    @Autowired
    private UserRepository userRepository;


    @Scheduled(cron = "0 * * * * *") // Run every min
    public void scheduleUserPuzzles() {
        List<User> users = userRepository.findAll();
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        users.forEach(user -> {
            // Convert user's chosen time to LocalTime
            LocalTime chosenTime = LocalTime.parse(user.getChosenTime());

            if (chosenTime.equals(now)) {
                puzzleService.fetchAndDeliverPuzzles(user);
            }
        });
    }
}
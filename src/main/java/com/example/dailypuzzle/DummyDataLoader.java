package com.example.dailypuzzle;

import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalTime;

@Configuration
public class DummyDataLoader {

    @Bean
    public CommandLineRunner loadDummyData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create dummy users
            User user1 = new User();
            user1.setUsername("user1");
            user1.setEmail("user1@example.com");
            user1.setPassword(passwordEncoder.encode("password1"));  // Encrypt the password
            user1.setRole("ROLE_USER");
            user1.setChosenTime(LocalTime.of(8, 30));

            User user2 = new User();
            user2.setUsername("admin");
            user2.setEmail("admin@example.com");
            user2.setPassword(passwordEncoder.encode("adminpass"));  // Encrypt the password
            user2.setRole("ROLE_ADMIN");
            user2.setChosenTime(LocalTime.of(23, 0));

            // Save the users to the database
            userRepository.save(user1);
            userRepository.save(user2);

            System.out.println("Dummy users added");
        };
    }
}

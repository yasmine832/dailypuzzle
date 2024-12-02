package com.example.dailypuzzle.service;


import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        public User registerUser(User user) {
            // encode pw before saving user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
//todo validation
            //save user todb
            return userRepository.save(user);
        }




    }
package com.example.dailypuzzle.service;


import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.UserRepository;
import exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        }


    public User registerUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email is already in use");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the database
        User savedUser = userRepository.save(user);

        // Authenticate the user right after registration
        authenticateUser(savedUser.getUsername(), user.getPassword());

        return savedUser;
    }

    public void authenticateUser(String username, String password) {
        // Clear any existing authentication in the context
        SecurityContextHolder.clearContext();


        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(authRequest);

        // Set the authentication in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
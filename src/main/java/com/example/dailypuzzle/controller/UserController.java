package com.example.dailypuzzle.controller;

import ch.qos.logback.core.model.Model;
import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.service.UserService;
import exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.ui.ModelMap;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid User user,
                                      Errors errors,
                                      Model model, ModelMap modelMap) {
        if (errors.hasErrors()) {
            return "register"; // If there are validation errors, re-display the registration form
        }

        // Validate if passwords match (simple check)
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            errors.rejectValue("matchingPassword", "passwords.not.match", "Passwords don't match");
            return "register";
        }

        try {
            userService.registerUser(user);
            return "redirect:/hello";  // Redirect after successful registration
        } catch (UserAlreadyExistsException ex) {
            modelMap.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
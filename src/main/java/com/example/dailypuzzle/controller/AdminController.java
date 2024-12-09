package com.example.dailypuzzle.controller;

import ch.qos.logback.core.model.Model;
import com.example.dailypuzzle.model.User;
import com.example.dailypuzzle.repository.UserRepository;
import com.example.dailypuzzle.service.AdminService;
import com.example.dailypuzzle.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final UserRepository userRepository;
    private final AdminService adminService;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository, AdminService adminService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public String listUsers(ModelMap modelMap) {
        modelMap.addAttribute("users", adminService.getAllUsers());
        return "admin/users";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam("username") String username, ModelMap modelMap) {
        try {
            System.out.println("Deleting user: " + username);
            adminService.deleteUser(username);
            modelMap.addAttribute("success", "User deleted successfully");
        } catch (Exception ex) {
            modelMap.addAttribute("error", ex.getMessage());
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/create-user")
    public String showCreateUserForm(ModelMap model) {
        model.addAttribute("userCreationDTO", new User());
        return "admin/create-user";
    }

    @PostMapping("/create-user")
    public String createUser(
            @Valid @ModelAttribute("userCreationDTO") User userDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Check for validation errors
            if (bindingResult.hasErrors()) {
                return "admin/create-user";
            }

            // Create the user
            User createdUser = adminService.createUser(userDTO);

            // Add success message
            redirectAttributes.addFlashAttribute("successMessage",
                    "User " + createdUser.getUsername() + " created successfully");

            return "redirect:/admin/users";
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/create-user";
        }
    }
}



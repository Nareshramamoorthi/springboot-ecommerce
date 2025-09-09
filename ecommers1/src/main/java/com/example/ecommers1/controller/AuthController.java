package com.example.ecommers1.controller;

import com.example.ecommers1.model.Role;
import com.example.ecommers1.model.User;
import com.example.ecommers1.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Show login page
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Show register page
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle registration
    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        user.setRole(Role.USER); // default new user role
        userService.register(user);
        return "redirect:/login";
    }
}

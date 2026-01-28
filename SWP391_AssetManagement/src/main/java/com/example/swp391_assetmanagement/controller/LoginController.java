package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.dto.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute LoginRequest request, HttpSession session, Model model) {
        if(userService.authenticate(request)){
            session.setAttribute("user", request.getUsername());
            return "redirect:/template";//sang menu chinh
        }
        else {
            model.addAttribute("error", "Incorrect username or password");
            return "login";
        }
    }
}

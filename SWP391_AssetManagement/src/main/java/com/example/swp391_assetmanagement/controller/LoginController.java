package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.LoginDTORequest;
import com.example.swp391_assetmanagement.dto.response.LoginDTOResponse;
import com.example.swp391_assetmanagement.usecase.LoginUsecase;
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

    private final LoginUsecase loginUsecase;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTORequest request, HttpSession session, Model model) {
        LoginDTOResponse userResponse = loginUsecase.executeLogin(request);

        if (userResponse != null) {
            session.setAttribute("USER_NAME", userResponse.getUserName());
            session.setAttribute("ROLE", userResponse.getRoleId());
            session.setAttribute("USER_CODE", userResponse.getUserCode());
            return "redirect:/ManagerViewAsset";
        }else {
            model.addAttribute("error", "Incorrect username or password");
            return "login";
        }
    }
}

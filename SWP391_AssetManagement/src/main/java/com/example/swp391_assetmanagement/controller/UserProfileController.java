package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.UpdateProfileDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewProfileDTOResponse;
import com.example.swp391_assetmanagement.dto.request.ChangePasswordDTORequest;
import com.example.swp391_assetmanagement.usecase.UpdateProfileUsecase;
import com.example.swp391_assetmanagement.usecase.ViewProfileUsecase;
import com.example.swp391_assetmanagement.usecase.ChangePasswordUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final ViewProfileUsecase viewProfileUsecase;
    private final UpdateProfileUsecase updateProfileUsecase;
    private final ChangePasswordUsecase changePasswordUsecase;

    @GetMapping
    public String viewProfile(HttpSession session, Model model) {
        ViewProfileDTOResponse response = viewProfileUsecase.execute(session);
        if (response == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("profileResponse", response);
        
        UpdateProfileDTORequest updateRequest = UpdateProfileDTORequest.builder()
                .name(response.getName())
                .phone(response.getPhone())
                .email(response.getEmail())
                .dateOfBirth(response.getDateOfBirth())
                .build();
        model.addAttribute("profileRequest", updateRequest);
        model.addAttribute("changePasswordRequest", ChangePasswordDTORequest.builder().build());
        
        return "profile/Profile";
    }

    @PostMapping("/edit")
    public String editProfile(@ModelAttribute("profileRequest") UpdateProfileDTORequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            updateProfileUsecase.execute(request, session);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("changePasswordRequest") ChangePasswordDTORequest request, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            changePasswordUsecase.execute(request, session);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/profile";
    }
}

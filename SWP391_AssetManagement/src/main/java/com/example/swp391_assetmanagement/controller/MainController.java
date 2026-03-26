package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.*;
import com.example.swp391_assetmanagement.dto.response.*;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.usecase.*;
import com.example.swp391_assetmanagement.usecase.ViewAssetLifecycleUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ViewAssetUsecase viewAssetUsecase;

    private final ManageAssetRequestProcessUsecase manageAssetRequestProcessUseCase;

    private final LoginUsecase loginUsecase;

    private final ViewAssetLifecycleUsecase viewAssetLifecycleUsecase;

    @GetMapping({"/", "/login"})
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/")
    public String login(@ModelAttribute LoginDTORequest request, HttpSession session, Model model) {
        LoginDTOResponse userResponse = loginUsecase.executeLogin(request);

        if (userResponse != null) {
            session.setAttribute("USER_NAME", userResponse.getUserName());
            session.setAttribute("ROLE", userResponse.getRoleId());
            session.setAttribute("USER_CODE", userResponse.getUserCode());
            session.setAttribute("LOCATION_ID", userResponse.getLocationId());
            if(Objects.equals(Roles.ADMIN.getValue(),userResponse.getRoleId())){
                return "redirect:/admin/viewUser";
            }
            if(Objects.equals(Roles.DEPARTMENT_MANAGER.getValue(),userResponse.getRoleId())){
                return "redirect:/viewRequest";
            }
            return "redirect:/viewAsset";
        }else {
            model.addAttribute("error", "Incorrect username or password");
            return "login";
        }
    }

    @GetMapping("/viewRequest")
    public String viewRequest(@ModelAttribute ViewAllProcessDTORequest request, HttpSession session, Model model) {

        ViewAllProcessDTOResponse response = manageAssetRequestProcessUseCase.viewAllProcess(request, session);
        model.addAttribute("data", response);

        return "RequestAllList";
    }

    @GetMapping("/viewAsset")
    public String viewAsset(@ModelAttribute ViewAssetDTORequest request, HttpSession session, Model model) {

        ViewAllAssetDTOResponse response = viewAssetUsecase.viewAsset(request, session);
        model.addAttribute("assets", response);

        return "ManagerViewAsset";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa toàn bộ dữ liệu trong session
        session.invalidate();

        // Điều hướng về trang login
        return "redirect:/";
    }

    @GetMapping("/viewAsset/lifecycle")
    public String viewAssetLifecycle(@ModelAttribute ViewAssetLifecycleDTORequest request, HttpSession session, Model model) {

        ViewAssetLifecycleDTOResponse response = viewAssetLifecycleUsecase.viewAssetLifecycle(request, session);
        model.addAttribute("lifecycle", response);

        return "AssetLifecycle";
    }
}

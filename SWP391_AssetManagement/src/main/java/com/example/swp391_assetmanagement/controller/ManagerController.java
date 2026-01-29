package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAssetRequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllAssetResponse;
import com.example.swp391_assetmanagement.usecase.ManagerUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerUsecase managerUsecase;

    @GetMapping("/viewAsset")
    public String viewAsset(@ModelAttribute ViewAssetRequest request, HttpSession session, Model model){

        ViewAllAssetResponse response = managerUsecase.viewAsset(request, session);
        model.addAttribute("assets", response);

        return "ManagerViewAsset";
    }
}

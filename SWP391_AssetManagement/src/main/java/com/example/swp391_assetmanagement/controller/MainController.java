package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAllProcessRequest;
import com.example.swp391_assetmanagement.dto.request.ViewInternalProcessRequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllProcessResponse;
import com.example.swp391_assetmanagement.dto.response.ViewInternalProcessAllResponse;
import com.example.swp391_assetmanagement.usecase.ManageAssetRequestProcessUseCase;
import com.example.swp391_assetmanagement.usecase.ManagerAssetInternalProcessUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {


//    private final ManagerAssetInternalProcessUsecase managerAssetInternalProcessUsecase;
//    @GetMapping("/viewRequest")
//    public String viewRequest(@ModelAttribute ViewInternalProcessRequest request, HttpSession session, Model model){
//
//        ViewInternalProcessAllResponse response = managerAssetInternalProcessUsecase.viewInternalProcess(request, session);
//        model.addAttribute("data", response);
//
//        return "RequestInternalList";
//    }

    private final ManageAssetRequestProcessUseCase manageAssetRequestProcessUseCase;

    @GetMapping("/viewRequest")
    public String viewRequest(@ModelAttribute ViewAllProcessRequest request, HttpSession session, Model model) {

        ViewAllProcessResponse response = manageAssetRequestProcessUseCase.viewAllProcess(request, session);
        model.addAttribute("data", response);

        return "RequestAllList";
    }
}

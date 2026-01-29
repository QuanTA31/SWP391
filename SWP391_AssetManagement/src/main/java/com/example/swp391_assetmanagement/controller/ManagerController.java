package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAssetRequest;
import com.example.swp391_assetmanagement.dto.request.ViewInternalProcessRequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllAssetResponse;
import com.example.swp391_assetmanagement.dto.response.ViewInternalProcessAllResponse;
import com.example.swp391_assetmanagement.usecase.ManagerAssetInternalProcessUsecase;
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

    private final ManagerAssetInternalProcessUsecase managerAssetInternalProcessUsecase;

    @GetMapping("/viewAsset")
    public String viewAsset(@ModelAttribute ViewAssetRequest request, HttpSession session, Model model){

        ViewAllAssetResponse response = managerUsecase.viewAsset(request, session);
        model.addAttribute("assets", response);

        return "ManagerViewAsset";
    }

    @GetMapping("/viewRequest")
    public String viewRequest(@ModelAttribute ViewInternalProcessRequest request, HttpSession session, Model model){

        ViewInternalProcessAllResponse response = managerAssetInternalProcessUsecase.viewInternalProcess(request, session);
        model.addAttribute("data", response);

        return "RequestList";
    }

//    @GetMapping("/viewRequestExternal")
//    public String viewRequest(){
//
////        ViewInternalProcessAllResponse response = managerAssetInternalProcessUsecase.viewInternalProcess(request, session);
////        model.addAttribute("data", response);
////
//        return "RequestListExternal";
//    }
}

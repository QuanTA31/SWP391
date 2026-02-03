package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAllProcessRequest;
import com.example.swp391_assetmanagement.dto.request.ViewAssetDTORequest;
import com.example.swp391_assetmanagement.dto.request.ViewExternalProcessRequest;
import com.example.swp391_assetmanagement.dto.request.ViewInternalProcessRequest;
import com.example.swp391_assetmanagement.dto.response.ViewAllAssetDTOResponse;
import com.example.swp391_assetmanagement.dto.response.ViewAllProcessResponse;
import com.example.swp391_assetmanagement.dto.response.ViewExternalProcessAllResponse;
import com.example.swp391_assetmanagement.dto.response.ViewInternalProcessAllResponse;
import com.example.swp391_assetmanagement.usecase.ManagerAssetExternalProcessUsecase;
import com.example.swp391_assetmanagement.usecase.ManagerAssetInternalProcessUsecase;
import com.example.swp391_assetmanagement.usecase.ViewAssetUsecase;
import com.example.swp391_assetmanagement.usecase.ManageAssetRequestProcessUseCase;
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

    private final ViewAssetUsecase viewAssetUsecase;

    private final ManageAssetRequestProcessUseCase manageAssetRequestProcessUseCase;

    private final ManagerAssetInternalProcessUsecase managerAssetInternalProcessUsecase;

    private final ManagerAssetExternalProcessUsecase managerAssetExternalProcessUsecase;

    @GetMapping("/viewRequest")
    public String viewRequest(@ModelAttribute ViewAllProcessRequest request, HttpSession session, Model model) {

        ViewAllProcessResponse response = manageAssetRequestProcessUseCase.viewAllProcess(request, session);
        model.addAttribute("data", response);

        return "RequestAllList";
    }

    @GetMapping("/viewInternalRequest")
    public String viewRequest(@ModelAttribute ViewInternalProcessRequest request, HttpSession session, Model model) {

        ViewInternalProcessAllResponse response = managerAssetInternalProcessUsecase.viewInternalProcess(request, session);
        model.addAttribute("internal", response);

        return "RequestInternalList";
    }

    @GetMapping("/viewExternalRequest")
    public String viewRequest(@ModelAttribute ViewExternalProcessRequest request, HttpSession session, Model model) {

        ViewExternalProcessAllResponse response = managerAssetExternalProcessUsecase.viewExternalProcess(request, session);
        model.addAttribute("external", response);

        return "RequestExternalList";
    }

    @GetMapping("/viewAsset")
    public String viewAsset(@ModelAttribute ViewAssetDTORequest request, HttpSession session, Model model) {

        ViewAllAssetDTOResponse response = viewAssetUsecase.viewAsset(request, session);
        model.addAttribute("assets", response);

        return "ManagerViewAsset";
    }
}

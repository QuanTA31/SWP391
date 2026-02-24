package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.*;
import com.example.swp391_assetmanagement.dto.response.*;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.usecase.*;
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

    private final ManagerAssetInternalProcessUsecase managerAssetInternalProcessUsecase;

    private final ManagerAssetExternalProcessUsecase managerAssetExternalProcessUsecase;

    private final LoginUsecase loginUsecase;

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
            if(Objects.equals(Roles.ADMIN.getValue(),userResponse.getRoleId())){
                return "redirect:/admin/viewUser";
            }
            return "redirect:/ManagerViewAsset";
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

//    @GetMapping("/viewInternalRequest")
//    public String viewRequest(@ModelAttribute ViewInternalProcessRequest request, HttpSession session, Model model) {
//
//        ViewInternalProcessAllResponse response = managerAssetInternalProcessUsecase.viewInternalProcess(request, session);
//        model.addAttribute("internal", response);
//
//        return "RequestInternalList";
//    }
//
//    @GetMapping("/viewExternalRequest")
//    public String viewRequest(@ModelAttribute ViewExternalProcessRequest request, HttpSession session, Model model) {
//
//        ViewExternalProcessAllResponse response = managerAssetExternalProcessUsecase.viewExternalProcess(request, session);
//        model.addAttribute("external", response);
//
//        return "RequestExternalList";
//    }

    // 1. Xem chi tiết Internal
    @GetMapping("/viewInternalRequest/detail")
    public String viewInternalDetail(@RequestParam("id") Long id, Model model) {

        ViewInternalProcessAllDTOResponse detailData = managerAssetInternalProcessUsecase.getDetailById(id);

        model.addAttribute("detailData", detailData);
        model.addAttribute("requestId", id);

        return "RequestInternalDetail";
    }

    // 2. Xem chi tiết External
    @GetMapping("/viewExternalRequest/detail")
    public String viewExternalDetail(@RequestParam("id") Long id, Model model) {

        ViewExternalProcessAllDTOResponse detailData = managerAssetExternalProcessUsecase.getDetailById(id);

        model.addAttribute("detailData", detailData);
        model.addAttribute("requestId", id);

        return "RequestExternalDetail";
    }

    @GetMapping("/viewAsset")
    public String viewAsset(@ModelAttribute ViewAssetDTORequest request, HttpSession session, Model model) {

        ViewAllAssetDTOResponse response = viewAssetUsecase.viewAsset(request, session);
        model.addAttribute("assets", response);

        return "ManagerViewAsset";
    }
}

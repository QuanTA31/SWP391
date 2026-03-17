package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAssetByUserDisabledDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAssetByUserDisabledDTOResponse;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.usecase.ExecuteRecoverUsecase;
import com.example.swp391_assetmanagement.usecase.ViewAssetByUserDisabledUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/recover-asset")
@RequiredArgsConstructor
public class RecoverAssetController {

    private final ViewAssetByUserDisabledUsecase viewAssetByUserDisabledUsecase;
    private final ExecuteRecoverUsecase executeRecoverUsecase;

    @GetMapping("/manager/recoverAsset")
    public String viewUser(@ModelAttribute ViewAssetByUserDisabledDTORequest request, HttpSession session, Model model) {

        ViewAssetByUserDisabledDTOResponse response = viewAssetByUserDisabledUsecase.viewAssetDisabled(request,session);
        model.addAttribute("assets", response);
        model.addAttribute("locations", Location.values());
        model.addAttribute("assetTypes", AssetType.values());

        return "recover_asset";
    }

    @PostMapping("/manager/execute-recover")
    public String executeRecover(@RequestParam("selectedAssetCodes") List<String> assetCodes,
                                 HttpSession session, RedirectAttributes ra) {

        String userCode = (String) session.getAttribute("USER_CODE");
        if (userCode == null) {
            return "redirect:/login";
        }

        try {
            executeRecoverUsecase.execute(assetCodes, userCode);
            ra.addFlashAttribute("message", "Thu hồi tài sản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/viewRequest";
    }
}

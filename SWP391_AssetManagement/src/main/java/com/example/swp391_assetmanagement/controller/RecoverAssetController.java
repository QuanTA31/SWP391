package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAssetByUserDisabledDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAssetByUserDisabledDTOResponse;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.usecase.ViewAssetByUserDisabledUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recover-asset")
@RequiredArgsConstructor
public class RecoverAssetController {

    private final ViewAssetByUserDisabledUsecase viewAssetByUserDisabledUsecase;

    @GetMapping("/manager/recoverAsset")
    public String viewUser(@ModelAttribute ViewAssetByUserDisabledDTORequest request, HttpSession session, Model model) {

        ViewAssetByUserDisabledDTOResponse response = viewAssetByUserDisabledUsecase.viewAssetDisabled(request,session);
        model.addAttribute("assets", response);
        model.addAttribute("locations", Location.values());
        model.addAttribute("assetTypes", AssetType.values());

        return "recover_asset";
    }

}

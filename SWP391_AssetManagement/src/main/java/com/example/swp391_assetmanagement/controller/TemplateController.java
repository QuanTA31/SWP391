package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.response.AssetDTOResponse;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TemplateController {

    private final AssetService assetService;

    @Autowired
    public TemplateController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/details/{productId}")
    public String detail(@PathVariable Long productId, Model model) {

        AssetResponse assetResponse = assetService.createAsset(AssetRequest.builder()
                .id(productId)
                .build());

        AssetDTOResponse response = AssetDTOResponse.builder()
                .assetCode(assetResponse.getAssetCode())
                .description(assetResponse.getDescription())
                .build();

        model.addAttribute("product", response);
        return "template";
    }
}

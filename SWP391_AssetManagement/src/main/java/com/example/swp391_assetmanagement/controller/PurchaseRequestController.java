package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ApprovalPurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.usecase.CreatePurchaseRequestUsecase;
import com.example.swp391_assetmanagement.usecase.GetPurchaseRequestManagerUsecase;
import com.example.swp391_assetmanagement.usecase.GetPurchaseRequestWarehouseUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/purchase-requests")
@RequiredArgsConstructor
public class PurchaseRequestController {

    private final CreatePurchaseRequestUsecase createPurchaseRequestUsecase;
    private final GetPurchaseRequestWarehouseUsecase getPurchaseRequestWarehouseUsecase;
    private final GetPurchaseRequestManagerUsecase getPurchaseRequestManagerUsecase;

    @GetMapping("/warehouse/view")
    public String viewPurchaseRequestForm(@RequestParam(required = false) Long assetRequestId, Model model, HttpSession session) {

        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = getPurchaseRequestWarehouseUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest",createPurchaseRequestDTORequest);
        model.addAttribute("assetTypes", AssetType.values());

        return "createPurchaseRequest";
    }

    @PostMapping("/warehouse/create")
    public String createPurchaseRequestForm(
            @ModelAttribute CreatePurchaseRequestDTORequest request, HttpSession session, Model model) {

        createPurchaseRequestUsecase.execute(request, session);

        return "createPurchaseRequest";
    }

    @GetMapping("/manager/view")
    public String managerViewPurchaseRequest(@RequestParam Long assetRequestId, Model model, HttpSession session) {
        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = getPurchaseRequestManagerUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest",createPurchaseRequestDTORequest);
        model.addAttribute("assetTypes", AssetType.values());
        model.addAttribute("approvalRequest",new ApprovalPurchaseRequestDTORequest());
        return "createPurchaseRequest";
    }

    @PostMapping("/manager/approval")
    public String managerViewPurchaseRequest(
            @ModelAttribute ApprovalPurchaseRequestDTORequest request, HttpSession session, Model model) {

        return "createPurchaseRequest";
    }
}

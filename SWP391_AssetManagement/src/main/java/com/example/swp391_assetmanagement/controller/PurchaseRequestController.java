package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ApprovalPurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.usecase.CreatePurchaseRequestUsecase;
import com.example.swp391_assetmanagement.usecase.ManagerCreatePurchaseRequestUsecase;
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
    private final ManagerCreatePurchaseRequestUsecase managerCreatePurchaseRequestUsecase;

    @GetMapping("/warehouse/view")
    public String viewPurchaseRequestForm(@RequestParam(required = false) Long assetRequestId, Model model, HttpSession session) {
       CreatePurchaseRequestDTORequest dto = new CreatePurchaseRequestDTORequest();

       if(assetRequestId == null) {
           dto.setCreatePurchaseRequestDetailDTORequestList(new ArrayList<>());
           dto.setSubmitted(false);
           dto.setAssetRequestId(null);
       }else{
           dto = createPurchaseRequestUsecase.getExistingRequest(assetRequestId);
           dto.setSubmitted(false);
       }
        model.addAttribute("purchaseRequest", dto);
        model.addAttribute("assetTypes", AssetType.values());

        return "createPurchaseRequest";
    }

    @PostMapping("/warehouse/create")
    public String createPurchaseRequestForm(
            @ModelAttribute CreatePurchaseRequestDTORequest request, HttpSession session) {

        createPurchaseRequestUsecase.execute(request, session);
        return "createPurchaseRequest";
    }

    @GetMapping("/manager/view")
    public String managerViewPurchaseRequest(@RequestParam Long assetRequestId, Model model, HttpSession session) {
        CreatePurchaseRequestDTORequest dto =
                createPurchaseRequestUsecase.getExistingRequest(assetRequestId);
        dto.setSubmitted(true);

        model.addAttribute("purchaseRequest", dto);
        model.addAttribute("assetTypes", AssetType.values());
        model.addAttribute("approvalRequest", new ApprovalPurchaseRequestDTORequest());
        return "createPurchaseRequest";
    }

    @PostMapping("/manager/approval")
    public String managerViewPurchaseRequest(
            @ModelAttribute ApprovalPurchaseRequestDTORequest request, HttpSession session) {

        managerCreatePurchaseRequestUsecase.execute(request,session);
        return "createPurchaseRequest";
    }
}

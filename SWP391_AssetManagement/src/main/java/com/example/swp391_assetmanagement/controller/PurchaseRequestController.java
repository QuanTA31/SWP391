package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ApprovalPurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.OptionDetailSelectDTORequest;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.usecase.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/purchase-requests")
@RequiredArgsConstructor
public class PurchaseRequestController {

    private final CreatePurchaseRequestUsecase createPurchaseRequestUsecase;
    private final GetPurchaseRequestWarehouseUsecase getPurchaseRequestWarehouseUsecase;
    private final GetPurchaseRequestManagerUsecase getPurchaseRequestManagerUsecase;
    private final ManagerCreatePurchaseRequestUsecase managerCreatePurchaseRequestUsecase;
    private final ManagerRejectAllOptionDetailUsecase managerRejectAllOptionDetailUsecase;

    @GetMapping("/warehouse/view")
    public String viewPurchaseRequestForm(@RequestParam(required = false) Long assetRequestId, Model model, HttpSession session) {

        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = getPurchaseRequestWarehouseUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest",createPurchaseRequestDTORequest);
        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> Map.of(
                                "value", a.getValue(),
                                "label", a.getName()
                        ))
                        .toList());

        return "createPurchaseRequest";
    }

    @PostMapping("/warehouse/create")
    public String createPurchaseRequestForm(
            @ModelAttribute CreatePurchaseRequestDTORequest request, HttpSession session, Model model) {

        createPurchaseRequestUsecase.execute(request, session);

        return "redirect:/viewRequest";
    }

    @GetMapping("/manager/view")
    public String managerViewPurchaseRequest(@RequestParam Long assetRequestId, Model model, HttpSession session) {
        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = getPurchaseRequestManagerUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest",createPurchaseRequestDTORequest);
        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> Map.of(
                                "value", a.getValue(),
                                "label", a.getName()
                        ))
                        .toList());        model.addAttribute("approvalRequest",new ApprovalPurchaseRequestDTORequest());
        return "createPurchaseRequest";
    }

    @PostMapping("/manager/approval")
    public String managerViewPurchaseRequest(
            @ModelAttribute ApprovalPurchaseRequestDTORequest request, HttpSession session, Model model) {

        managerCreatePurchaseRequestUsecase.execute(request, session);
        return "redirect:/viewRequest";
    }

    @PostMapping("/manager/optionDetailRejectAll")
    public String managerOptionDetail(@RequestParam Long assetRequestId, HttpSession session, Model model) {

        managerRejectAllOptionDetailUsecase.execute(assetRequestId, session);
        return "redirect:/viewRequest";
    }
}

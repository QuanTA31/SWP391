package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.common.RoleChecker;
import com.example.swp391_assetmanagement.dto.request.*;
import com.example.swp391_assetmanagement.dto.response.LiquiDateCreateDTOResponse;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.usecase.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/liquidate")
@RequiredArgsConstructor
public class LiquidateController {

    private final CreateLiquidationRequestUsecase createLiquidationRequestUsecase;//xong
    private final LiquiAssetManagerUsecase liquiAssetManagerUsecase;//xong
    private final GetLiquidationManagerUsecase getLiquidationManagerUsecase;
    private final GetLiquidationPurchasingUsecase getLiquidationPurchasingUsecase;//xong
    private final ManagerCreateLiquidationUsecase managerCreateLiquidationUsecase;//xong
    private final ManagerRejectAllLiquidationUsecase managerRejectAllOptionDetailUsecase;
    private final UpdateAssetRequestLiquidationUsecase updateAssetRequestUsecase;
    private final MoveAssetRequestToInProgressUsecase moveAssetRequestToInProgressUsecase;
    private final MoveAssetRequestToCompletedUsecase moveAssetRequestToCompletedUsecase;


    private final RoleChecker roleChecker;

    // Manager access to screen view asset can liquidate
    @GetMapping("/manager/viewAsset")
    public String viewAssets(@ModelAttribute LiquiDateCreateDTORequest liquiDateCreateDTORequest, HttpSession session, Model model) {

//        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        LiquiDateCreateDTOResponse assets = liquiAssetManagerUsecase.execute(liquiDateCreateDTORequest, session);
        model.addAttribute("assets", assets);

        return "createLiquidationRequest";
    }

    // Manager access to screen view asset and create request
    @PostMapping("/manager/create")
    public String createLiquidationRequestForm(
            @RequestParam(value = "selectedAssetIds", required = false) List<Long> requests, HttpSession session) {

//        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        createLiquidationRequestUsecase.execute(requests, session);

        return "redirect:/liquidate/manager/viewAsset";
    }

    @GetMapping("/manager/view")
    public String managerViewPurchaseRequest(
            @RequestParam Long assetRequestId,
            @ModelAttribute LiquiDateCreateDTORequest liquiDateCreateDTORequest,
            Model model,
            HttpSession session) {
        CreateLiquidationDTORequest createLiquidationDTORequest =
                getLiquidationManagerUsecase.execute(assetRequestId);
        LiquiDateCreateDTOResponse assets =
                liquiAssetManagerUsecase.execute(liquiDateCreateDTORequest, session);
        model.addAttribute("assets", assets);
        model.addAttribute("liquidationRequest", createLiquidationDTORequest);
        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> AssetTypeDTORequest.builder()
                                .value(a.getValue())
                                .label(a.getName())
                                .build())
                        .toList());
        model.addAttribute("approvalRequest",
                ApprovalPurchaseRequestDTORequest.builder().build());
        model.addAttribute("role", session.getAttribute("ROLE"));
        return "viewLiquidationRequest";
    }

    @PostMapping("/manager/approval")
    public String approveLiquidation(

            @ModelAttribute ApprovalLiquidationDTORequest request, HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);
        managerCreateLiquidationUsecase.execute(request, session);
        return "redirect:/viewRequest";
    }

    @PostMapping("/manager/optionDetailRejectAll")
    public String managerOptionDetail(@RequestParam Long assetRequestDetailId, HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);
        managerRejectAllOptionDetailUsecase.execute(assetRequestDetailId, session);
        return "redirect:/viewRequest";
    }

    private final CreateLiquidationOptionUsecase createUseCase;
    private final EditLiquidationOptionUsecase editUseCase;
    private final ApproveLiquidationOptionUsecase approveUseCase;
    private final DeleteLiquidationOptionUsecase deleteUseCase;
    private final GetLiquidationOptionListUsecase getOptionDetailListUseCase;

    // ================= CREATE =================
    @PostMapping("/option-detail/create")
    public String create(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                         @ModelAttribute("createForm") OptionDetailFormDTORequest form,
                         @RequestParam("assetRequestId") Long assetRequestId,
                         HttpSession session,
                         Model model) {
        try {
            createUseCase.execute(requestDetailId, form, session);
            model.addAttribute("createForm", new OptionDetailFormDTORequest());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("createForm", form);
        }
        getOptionDetailListUseCase.loadToModel(requestDetailId, null, null, session, model);
        model.addAttribute("editForm", new OptionDetailFormDTORequest());
        return "redirect:/liquidate/option-detail/list"
                + "?asset_external_request_detail_id=" + requestDetailId
                + "&assetRequestId=" + assetRequestId;
    }

    // ================= LIST =================
    @GetMapping("/option-detail/list")
    public String list(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "page", required = false) Integer page,
                       @RequestParam("assetRequestId") Long assetRequestId,
                       HttpSession session, Model model) {
        model.addAttribute("assetRequestId", assetRequestId);
        model.addAllAttributes(getOptionDetailListUseCase.execute(requestDetailId, status, page, session).toModel());
        model.addAttribute("createForm", model.containsAttribute("createForm") ? model.getAttribute("createForm") : new OptionDetailFormDTORequest());
        model.addAttribute("editForm", model.containsAttribute("editForm") ? model.getAttribute("editForm") : new OptionDetailFormDTORequest());
        return "optiondetail/listLiquidation";
    }

    // ================= EDIT =================
    @PostMapping("/option-detail/edit/{id}")
    public String edit(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @ModelAttribute("editForm") OptionDetailFormDTORequest form,
                       @RequestParam("assetRequestId") Long assetRequestId,
                       HttpSession session,
                       Model model) {
        try {
            editUseCase.execute(requestDetailId, form, session);
            model.addAttribute("editForm", new OptionDetailFormDTORequest());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("editErrorMessage", ex.getMessage());
            model.addAttribute("editForm", form);
            model.addAttribute("openEditModal", true);
        }
        getOptionDetailListUseCase.loadToModel(requestDetailId, null, null, session, model);
        model.addAttribute("createForm", new OptionDetailFormDTORequest());
        return "redirect:/liquidate/option-detail/list"
                + "?asset_external_request_detail_id=" + requestDetailId
                + "&assetRequestId=" + assetRequestId;
    }

    // ================= DELETE =================
    @PostMapping("/option-detail/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                         @RequestParam("assetRequestId") Long assetRequestId,
                         HttpSession session) {
        deleteUseCase.execute(requestDetailId, id, session);
        return "redirect:/liquidate/option-detail/list"
                + "?asset_external_request_detail_id=" + requestDetailId
                + "&assetRequestId=" + assetRequestId;
    }

    @PostMapping("/purchasing/research")
    public String movetoResearch(@RequestParam Long assetRequestId,
                                 HttpSession session) {
        updateAssetRequestUsecase.execute(assetRequestId, session);
        return "redirect:/viewRequest";
    }

    // ================= APPROVAL =================
    @PostMapping("/option-detail/approval")
    public String approve(
            @RequestParam("id") Long optionId,
            @RequestParam("asset_external_request_detail_id") Long requestDetailId,
            @RequestParam("assetRequestId") Long assetRequestId,
            HttpSession session
    ) {
        approveUseCase.execute(optionId, requestDetailId, true, session);

        return "redirect:/liquidate/option-detail/list"
                + "?asset_external_request_detail_id=" + requestDetailId
                + "&assetRequestId=" + assetRequestId;
    }

    // ==================== Purchasing View Liquidate ===============

    @GetMapping("/purchasing/view")
    public String viewPurchaseRequestForm(
            @RequestParam Long assetRequestId,
            Model model,
            HttpSession session) {
        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.PURCHASING);
        CreateLiquidationDTORequest dto =
                getLiquidationPurchasingUsecase.execute(assetRequestId);
        model.addAttribute("liquidationRequest", dto);
        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> AssetTypeDTORequest.builder()
                                .value(a.getValue())
                                .label(a.getName())
                                .build())
                        .toList());
        model.addAttribute("role", session.getAttribute("ROLE"));
        return "viewLiquidationRequest";
    }

    // ==================== IN_PROGRESS ===============
    @PostMapping("/purchasing/progress")
    public String progress(@RequestParam Long requestId,
                           HttpSession session) {
        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.PURCHASING);

        moveAssetRequestToInProgressUsecase.execute(requestId, session);
        return "redirect:/viewRequest";
    }

    // ==================== COMPLETED ===============
    @PostMapping("/purchasing/complete")
    public String complete(@RequestParam Long requestId,
                           HttpSession session) {
        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.PURCHASING);

        moveAssetRequestToCompletedUsecase.execute(requestId, session);
        return "redirect:/viewRequest";
    }
}

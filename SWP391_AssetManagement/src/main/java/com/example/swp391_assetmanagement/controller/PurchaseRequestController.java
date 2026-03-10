package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.common.RoleChecker;
import com.example.swp391_assetmanagement.dto.request.*;
import com.example.swp391_assetmanagement.dto.request.*;
import com.example.swp391_assetmanagement.dto.response.ViewPurchaseAssetAllDTOResponse;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Roles;
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
    private final UpdateAssetRequestUsecase updateAssetRequestUsecase;
    private final WarehouseCreateAssetsUsecase warehouseCreateAssetsUsecase;
    private final MoveAssetRequestToInProgressUsecase moveAssetRequestToInProgressUsecase;
    private final ViewPurchaseAssetAllUsecase viewPurchaseAssetAllUsecase;
    private final WarehouseCompleteUsecase warehouseCompleteUsecase;

    private final RoleChecker roleChecker;

    @GetMapping("/warehouse/view")
    public String viewPurchaseRequestForm(@RequestParam(required = false) Long assetRequestId, Model model, HttpSession session) {

        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = getPurchaseRequestWarehouseUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest",createPurchaseRequestDTORequest);
        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> new AssetTypeDTORequest(a.getValue(), a.getName()))
                        .toList());
        model.addAttribute("role",session.getAttribute("ROLE"));
        return "createPurchaseRequest";
    }

    @PostMapping("/warehouse/create")
    public String createPurchaseRequestForm(
            @ModelAttribute CreatePurchaseRequestDTORequest request, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.WAREHOUSE);

        createPurchaseRequestUsecase.execute(request, session);

        return "redirect:/viewRequest";
    }

    @GetMapping("/manager/view")
    public String managerViewPurchaseRequest(@RequestParam Long assetRequestId, Model model, HttpSession session) {
        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = getPurchaseRequestManagerUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest",createPurchaseRequestDTORequest);
        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> new AssetTypeDTORequest(a.getValue(), a.getName()))
                        .toList());
        model.addAttribute("approvalRequest",new ApprovalPurchaseRequestDTORequest());
        model.addAttribute("role", session.getAttribute("ROLE"));
        return "createPurchaseRequest";
    }

    @PostMapping("/manager/approval")
    public String managerViewPurchaseRequest(
            @ModelAttribute ApprovalPurchaseRequestDTORequest request, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);
        managerCreatePurchaseRequestUsecase.execute(request, session);
        return "redirect:/viewRequest";
    }

    @PostMapping("/manager/optionDetailRejectAll")
    public String managerOptionDetail(@RequestParam Long assetRequestDetailId, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);
        managerRejectAllOptionDetailUsecase.execute(assetRequestDetailId, session);
        return "redirect:/viewRequest";
    }

    private final CreateOptionDetailUsecase createUseCase;
    private final EditOptionDetailUsecase editUseCase;
    private final ApproveOptionDetailUsecase approveUseCase;
    private final DeleteOptionDetailUsecase deleteUseCase;
    private final GetOptionDetailListUsecase getOptionDetailListUseCase;

    // ================= CREATE =================
    @PostMapping("/option-detail/create")
    public String create(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                         @ModelAttribute("createForm") OptionDetailFormDTORequest form,
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
        return "optiondetail/list";
    }

    // ================= LIST =================
    @GetMapping("/option-detail/list")
    public String list(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "page", required = false) Integer page,
                       HttpSession session, Model model) {
        model.addAllAttributes(getOptionDetailListUseCase.execute(requestDetailId, status, page, session).toModel());
        model.addAttribute("createForm", model.containsAttribute("createForm") ? model.getAttribute("createForm") : new OptionDetailFormDTORequest());
        model.addAttribute("editForm", model.containsAttribute("editForm") ? model.getAttribute("editForm") : new OptionDetailFormDTORequest());
        return "optiondetail/list";
    }

    // ================= EDIT =================
    @PostMapping("/option-detail/edit/{id}")
    public String edit(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @ModelAttribute("editForm") OptionDetailFormDTORequest form,
                       HttpSession session,
                       Model model) {
        try {
            editUseCase.execute(form, session);
            model.addAttribute("editForm", new OptionDetailFormDTORequest());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("editErrorMessage", ex.getMessage());
            model.addAttribute("editForm", form);
            model.addAttribute("openEditModal", true);
        }
        getOptionDetailListUseCase.loadToModel(requestDetailId, null, null, session, model);
        model.addAttribute("createForm", new OptionDetailFormDTORequest());
        return "optiondetail/list";
    }

    // ================= DELETE =================
    @PostMapping("/option-detail/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                         HttpSession session) {
        deleteUseCase.execute(id, session);
        return "redirect:/purchase-requests/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    @PostMapping("/purchasing/research")
    public String delete(@RequestParam  Long assetRequestId,
                         HttpSession session) {
        updateAssetRequestUsecase.execute(assetRequestId, session);
        return "redirect:/viewRequest";
    }

    // ================= APPROVAL =================
    @PostMapping("/option-detail/approval")
    public String approve(@RequestParam("id") Long optionId,
                          @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                          HttpSession session
                          //@RequestParam(value = "selected", required = false) String selected
    ) {
        approveUseCase.execute(optionId, requestDetailId,true, session);
        return "redirect:/purchase-requests/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    @PostMapping("/warehouse/createAssets")
    public String createAssets(@RequestParam  Long assetRequestId,
                         HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.WAREHOUSE);
        warehouseCreateAssetsUsecase.execute(assetRequestId, session);
        return "redirect:/purchase-requests/viewPurchaseAsset?assetRequestId=" + assetRequestId;
    }

    // ==================== IN_PROGRESS ===============
    @PostMapping("/purchasing/progress")
    public String progress(@RequestParam  Long requestId,
                         HttpSession session) {
        moveAssetRequestToInProgressUsecase.execute(requestId, session);
        return "redirect:/viewRequest";
    }

    // ==================== STOCK_IN =====================
    @GetMapping("/viewPurchaseAsset")
    public String viewPurchaseAsset(@ModelAttribute ViewPurchaseAssetDTORequest request, HttpSession session, Model model) {

        ViewPurchaseAssetAllDTOResponse response = viewPurchaseAssetAllUsecase.viewPurchaseAssetAllDTOResponse(request, session);
        model.addAttribute("purchaseAsset", response);

        return "PurchaseAssetList";
    }
    @PostMapping("/warehouse/complete")
    public String wareHouseComplete(
            @RequestParam Long assetRequestId, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.WAREHOUSE);
        warehouseCompleteUsecase.execute(assetRequestId,session);
        return "redirect:/viewRequest";
    }
}

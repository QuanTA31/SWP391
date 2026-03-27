package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.common.RoleChecker;
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

import java.util.Arrays;

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

    private final CreatePurchaseOptionUsecase createUseCase;
    private final EditPurchaseOptionUsecase editUseCase;
    private final ApprovePurchaseOptionUsecase approveUseCase;
    private final DeletePurchaseOptionUsecase deleteUseCase;
    private final GetPurchaseOptionListUsecase getPurchaseOptionListUseCase;

    private final RoleChecker roleChecker;

    // View request detail
    // Author : PhatNV
    @GetMapping("/warehouse/view")
    public String viewPurchaseRequestForm(
            @RequestParam(required = false) Long assetRequestId, Model model, HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(),
                Roles.MANAGER, Roles.PURCHASING, Roles.WAREHOUSE);

        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest =
                getPurchaseRequestWarehouseUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest", createPurchaseRequestDTORequest);

        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> AssetTypeDTORequest.builder()
                                .value(a.getValue())
                                .label(a.getName())
                                .build())
                        .toList());

        model.addAttribute("role", session.getAttribute("ROLE"));

        return "createPurchaseRequest";
    }

    // Manager create request
    // Author : QuanTA
    @PostMapping("/manager/create")
    public String createPurchaseRequestForm(
            @ModelAttribute CreatePurchaseRequestDTORequest request, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        createPurchaseRequestUsecase.execute(request, session);

        return "redirect:/viewRequest";
    }

    // Manager view request detail
    // Author : PhatNV
    @GetMapping("/manager/view")
    public String managerViewPurchaseRequest(@RequestParam Long assetRequestId, Model model, HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest =
                getPurchaseRequestManagerUsecase.execute(assetRequestId);

        model.addAttribute("purchaseRequest", createPurchaseRequestDTORequest);

        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> AssetTypeDTORequest.builder()
                                .value(a.getValue())
                                .label(a.getName())
                                .build())
                        .toList());

        model.addAttribute("approvalRequest", ApprovalPurchaseRequestDTORequest.builder().build());

        model.addAttribute("role", session.getAttribute("ROLE"));

        return "createPurchaseRequest";
    }

    // Manager confirm request
    // Author : QuanTA
    @PostMapping("/manager/approval")
    public String managerViewPurchaseRequest(
            @ModelAttribute ApprovalPurchaseRequestDTORequest request, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        managerCreatePurchaseRequestUsecase.execute(request, session);

        return "redirect:/viewRequest";
    }

    // ================= CREATE =================
    // Purchase create option detail
    // Author : TuanNT
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

        getPurchaseOptionListUseCase.loadToModel(requestDetailId, null, null, session, model);

        model.addAttribute("editForm", new OptionDetailFormDTORequest());

        return "optiondetail/listPurchase";
    }

    // ================= LIST =================
    // View option detail of 1 request detail
    // Author : TuanNT
    @GetMapping("/option-detail/list")
    public String list(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @RequestParam(value = "status", required = false) String status,
                       @RequestParam(value = "page", required = false) Integer page,
                       HttpSession session, Model model) {

        model.addAllAttributes(getPurchaseOptionListUseCase.execute(requestDetailId, status, page, session).toModel());

        model.addAttribute("createForm", model.containsAttribute("createForm")
                ? model.getAttribute("createForm")
                : new OptionDetailFormDTORequest());

        model.addAttribute("editForm", model.containsAttribute("editForm")
                ? model.getAttribute("editForm")
                : new OptionDetailFormDTORequest());

        return "optiondetail/listPurchase";
    }

    // ================= EDIT =================
    // Edit option detail of 1 request detail
    // Author : TuanNT
    @PostMapping("/option-detail/edit/{id}")
    public String edit(@RequestParam("asset_external_request_detail_id") Long requestDetailId,
                       @ModelAttribute("editForm") OptionDetailFormDTORequest form,
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

        getPurchaseOptionListUseCase.loadToModel(requestDetailId, null, null, session, model);

        model.addAttribute("createForm", new OptionDetailFormDTORequest());

        return "optiondetail/listPurchase";
    }

    // ================= DELETE =================
    // Delete option detail of 1 request detail
    // Author : TuanNT
    @PostMapping("/option-detail/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                         HttpSession session) {

        deleteUseCase.execute(requestDetailId, id, session);

        return "redirect:/purchase-requests/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    // Purchase done research option detail off request
    // Author : LongNT
    @PostMapping("/purchasing/research")
    public String delete(@RequestParam Long assetRequestId, HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.PURCHASING);

        updateAssetRequestUsecase.execute(assetRequestId, session);

        return "redirect:/viewRequest";
    }

    // ================= APPROVAL =================
    // Manager approval 1 option detail per 1 request detail
    // Author : TuanNV
    @PostMapping("/option-detail/approval")
    public String approve(@RequestParam("id") Long optionId,
                          @RequestParam("asset_external_request_detail_id") Long requestDetailId,
                          HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        approveUseCase.execute(optionId, requestDetailId, true, session);

        return "redirect:/purchase-requests/option-detail/list?asset_external_request_detail_id=" + requestDetailId;
    }

    // Manager reject all option detail per 1 request detail
    // Author : QuanTA
    @PostMapping("/manager/optionDetailRejectAll")
    public String managerOptionDetail(@RequestParam Long assetRequestDetailId, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        managerRejectAllOptionDetailUsecase.execute(assetRequestDetailId, session);

        return "redirect:/viewRequest";
    }

    // ==================== IN_PROGRESS ===============
    // Purchase update status progress
    // Author : LongNT
    @PostMapping("/purchasing/progress")
    public String progress(@RequestParam Long requestId,
                           HttpSession session) {
        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.PURCHASING);

        moveAssetRequestToInProgressUsecase.execute(requestId, session);

        return "redirect:/viewRequest";
    }

    // Warehouse insert assets follow request
    // Author : QuanTA
    @PostMapping("/warehouse/createAssets")
    public String createAssets(@RequestParam Long assetRequestId,
                               HttpSession session) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.WAREHOUSE);

        warehouseCreateAssetsUsecase.execute(assetRequestId, session);

        return "redirect:/purchase-requests/viewPurchaseAsset?assetRequestId=" + assetRequestId;
    }

    // ==================== STOCK_IN =====================
    // View assets from request
    // Author : LongNT
    @GetMapping("/viewPurchaseAsset")
    public String viewPurchaseAsset(@ModelAttribute ViewPurchaseAssetDTORequest request,
                                    HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.WAREHOUSE);

        ViewPurchaseAssetAllDTOResponse response =
                viewPurchaseAssetAllUsecase.viewPurchaseAssetAllDTOResponse(request, session);

        model.addAttribute("purchaseAsset", response);

        return "PurchaseAssetList";
    }

    // Warehouse complete request
    @PostMapping("/warehouse/complete")
    public String wareHouseComplete(
            @RequestParam Long assetRequestId, HttpSession session, Model model) {

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.WAREHOUSE);

        warehouseCompleteUsecase.execute(assetRequestId, session);

        return "redirect:/viewRequest";
    }
}

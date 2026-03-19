package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.CreateMaintenanceRequestDTORequest;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;
import com.example.swp391_assetmanagement.usecase.CreateMaintenanceRequestUsecase;
import com.example.swp391_assetmanagement.usecase.GetAssetsForRepairUsecase;
import com.example.swp391_assetmanagement.usecase.GetMaintenanceRequestDetailUsecase;
import com.example.swp391_assetmanagement.usecase.UpdateMaintenanceRequestUsecase;
import com.example.swp391_assetmanagement.usecase.ConfirmMaintenanceRepairUsecase;
import com.example.swp391_assetmanagement.usecase.FinishMaintenanceRepairUsecase;
import com.example.swp391_assetmanagement.usecase.ConfirmMaintenanceReceiptUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/maintenance-requests")
@RequiredArgsConstructor
public class MaintenanceRequestController {

    private final GetAssetsForRepairUsecase getAssetsForRepairUsecase;
    private final CreateMaintenanceRequestUsecase createMaintenanceRequestUsecase;
    private final GetMaintenanceRequestDetailUsecase getMaintenanceRequestDetailUsecase;
    private final UpdateMaintenanceRequestUsecase updateMaintenanceRequestUsecase;
    private final ConfirmMaintenanceRepairUsecase confirmMaintenanceRepairUsecase;
    private final FinishMaintenanceRepairUsecase finishMaintenanceRepairUsecase;
    private final ConfirmMaintenanceReceiptUsecase confirmMaintenanceReceiptUsecase;

    //get to display maintain request form
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {

        requireDepartmentManager(session);

        List<AssetForRepairServiceResponse> assets = getAssetsForRepairUsecase.execute(session);

        model.addAttribute("assets", assets);
        model.addAttribute("request", new CreateMaintenanceRequestDTORequest());
        model.addAttribute("role", session.getAttribute("ROLE"));

        return "CreateMaintenanceRequest";
    }

    // post draft or submit
    @PostMapping("/create")
    public String handleCreate(
            @ModelAttribute("request") CreateMaintenanceRequestDTORequest request,
            HttpSession session,
            Model model) {

        requireDepartmentManager(session);

        try {
            createMaintenanceRequestUsecase.execute(request, session);
        } catch (ResponseStatusException ex) {
            // Trả lại form với thông báo lỗi
            List<AssetForRepairServiceResponse> assets = getAssetsForRepairUsecase.execute(session);
            model.addAttribute("assets", assets);
            model.addAttribute("request", request);
            model.addAttribute("role", session.getAttribute("ROLE"));
            model.addAttribute("errorMessage", ex.getReason());
            return "CreateMaintenanceRequest";
        }

        return "redirect:/viewRequest";
    }

    //view only
    @GetMapping("/{rolePath}/view")
    public String showViewOrEditForm(
            @PathVariable("rolePath") String rolePath,
            @RequestParam("assetRequestId") Long assetRequestId, 
            Model model, 
            HttpSession session) {
        
        List<AssetForRepairServiceResponse> assets = getAssetsForRepairUsecase.execute(session);

        String userCode = (String) session.getAttribute("USER_CODE");

        GetMaintenanceRequestDetailUsecase.MaintenanceRequestDetailResult result = 
                getMaintenanceRequestDetailUsecase.execute(assetRequestId, userCode);

        model.addAttribute("assets", assets);
        model.addAttribute("request", result.getDto());
        model.addAttribute("requestId", assetRequestId);
        model.addAttribute("requestStatusId", result.getRequestStatusId());
        model.addAttribute("isOwner", result.isOwner());
        model.addAttribute("role", session.getAttribute("ROLE"));
        model.addAttribute("rolePath", rolePath);
        model.addAttribute("assetInfo", result.getAsset());
        model.addAttribute("assetRequestInfo", result.getAssetRequest());
        model.addAttribute("requesterName", result.getRequesterName());

        if ("department_manager".equals(rolePath) && "01".equals(result.getRequestStatusId())) {
            return "CreateMaintenanceRequest"; 
        }

        return "MaintainRequestDetail"; 
    }

   // for draft update
    @PostMapping("/update")
    public String handleUpdate(
            @ModelAttribute("request") CreateMaintenanceRequestDTORequest request,
            @RequestParam("requestId") Long assetRequestId,
            HttpSession session,
            Model model) {

        requireDepartmentManager(session);

        String userCode = (String) session.getAttribute("USER_CODE");

        try {
            updateMaintenanceRequestUsecase.execute(assetRequestId, request, userCode);
        } catch (ResponseStatusException ex) {
            List<AssetForRepairServiceResponse> assets = getAssetsForRepairUsecase.execute(session);
            model.addAttribute("assets", assets);
            model.addAttribute("request", request);
            model.addAttribute("requestId", assetRequestId);
            
            // Re-fetch status since it might failed before status change
            GetMaintenanceRequestDetailUsecase.MaintenanceRequestDetailResult result = 
                getMaintenanceRequestDetailUsecase.execute(assetRequestId, userCode);
            model.addAttribute("requestStatusId", result.getRequestStatusId());
            model.addAttribute("isOwner", result.isOwner());
            
            model.addAttribute("role", session.getAttribute("ROLE"));
            model.addAttribute("errorMessage", ex.getReason());
            return "CreateMaintenanceRequest";
        }

        return "redirect:/viewRequest";
    }

    // ---------------------------------------------------------------
    private void requireDepartmentManager(HttpSession session) {
        Object role = session.getAttribute("ROLE");
        if (!Objects.equals(role, Roles.DEPARTMENT_MANAGER.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Chỉ Department Manager mới có quyền!");
        }
    }

    private void requireWarehouse(HttpSession session) {
        Object role = session.getAttribute("ROLE");
        if (!Objects.equals(role, Roles.WAREHOUSE.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Chỉ Warehouse mới có quyền thực hiện thao tác này!");
        }
    }

    @PostMapping("/warehouse/confirm-repair")
    public String confirmRepair(@RequestParam("requestId") Long assetRequestId, HttpSession session) {
        requireWarehouse(session);
        confirmMaintenanceRepairUsecase.execute(assetRequestId);
        return "redirect:/maintenance-requests/warehouse/view?assetRequestId=" + assetRequestId;
    }

    @PostMapping("/warehouse/finish-repair")
    public String finishRepair(
            @RequestParam("requestId") Long assetRequestId, 
            @RequestParam("action") String action,
            HttpSession session) {
        requireWarehouse(session);
        boolean isSuccess = "OK".equals(action);
        finishMaintenanceRepairUsecase.execute(assetRequestId, isSuccess);
        return "redirect:/maintenance-requests/warehouse/view?assetRequestId=" + assetRequestId;
    }

    @PostMapping("/department_manager/confirm-receive")
    public String confirmReceive(@RequestParam("requestId") Long assetRequestId, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            confirmMaintenanceReceiptUsecase.execute(assetRequestId);
            redirectAttributes.addFlashAttribute("successMessage", "Xác nhận nhận lại tài sản thành công. Yêu cầu hoàn tất.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi lập biên bản nhận lại: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }
}

package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.CreateMaintenanceRequestDTORequest;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;
import com.example.swp391_assetmanagement.usecase.CreateMaintenanceRequestUsecase;
import com.example.swp391_assetmanagement.usecase.GetAssetsForRepairUsecase;
import com.example.swp391_assetmanagement.usecase.GetMaintenanceRequestDetailUsecase;
import com.example.swp391_assetmanagement.usecase.UpdateMaintenanceRequestUsecase;
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

    /**
     * GET /maintenance-requests/create
     * Hiển thị form tạo yêu cầu sửa chữa cho Department Manager.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {

        requireDepartmentManager(session);

        List<AssetForRepairServiceResponse> assets = getAssetsForRepairUsecase.execute(session);

        model.addAttribute("assets", assets);
        model.addAttribute("request", new CreateMaintenanceRequestDTORequest());
        model.addAttribute("role", session.getAttribute("ROLE"));

        return "CreateMaintenanceRequest";
    }

    /**
     * POST /maintenance-requests/create
     * Xử lý submit form: lưu nháp hoặc gửi ngay (APPROVED).
     */
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

    /**
     * GET /maintenance-requests/view?assetRequestId=...
     * Hiển thị chi tiết (view-only) hoặc màn hình sửa (nếu đang là DRAFT).
     */
    @GetMapping("/view")
    public String showViewOrEditForm(@RequestParam("assetRequestId") Long assetRequestId, Model model, HttpSession session) {
        
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

        return "CreateMaintenanceRequest"; // Tận dụng chung 1 template
    }

    /**
     * POST /maintenance-requests/update
     * Cập nhật request (chỉ cho DRAFT).
     */
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
                    "Chỉ Department Manager mới có quyền tạo yêu cầu sửa chữa!");
        }
    }
}

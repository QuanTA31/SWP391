package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.CreateMaintenanceRequestDTORequest;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;
import com.example.swp391_assetmanagement.usecase.CreateMaintenanceRequestUsecase;
import com.example.swp391_assetmanagement.usecase.GetAssetsForRepairUsecase;
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

    // ---------------------------------------------------------------
    private void requireDepartmentManager(HttpSession session) {
        Object role = session.getAttribute("ROLE");
        if (!Objects.equals(role, Roles.DEPARTMENT_MANAGER.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Chỉ Department Manager mới có quyền tạo yêu cầu sửa chữa!");
        }
    }
}

package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAssetByUserDisabledDTORequest;
import com.example.swp391_assetmanagement.dto.response.RecoverProcessDTOResponse;
import com.example.swp391_assetmanagement.dto.response.ViewAssetByUserDisabledDTOResponse;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.usecase.WarehouseRecoverUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/recover-asset")
@RequiredArgsConstructor
public class RecoverAssetController {

    private final WarehouseRecoverUsecase warehouseRecoverUsecase;

    @GetMapping("/manager/recoverAsset")
    public String viewUser(@ModelAttribute ViewAssetByUserDisabledDTORequest request, HttpSession session, Model model) {

        ViewAssetByUserDisabledDTOResponse response = warehouseRecoverUsecase.viewAssetDisabled(request,session);
        model.addAttribute("assets", response);
        model.addAttribute("locations", Location.values());
        model.addAttribute("assetTypes", AssetType.values());

        return "recover_asset";
    }

    @PostMapping("/manager/execute-recover")
    public String executeRecover(@RequestParam("selectedAssetCodes") List<String> assetCodes,
                                 HttpSession session, RedirectAttributes ra) {

        String userCode = (String) session.getAttribute("USER_CODE");
        if (userCode == null) {
            return "redirect:/login";
        }

        try {
            warehouseRecoverUsecase.execute(assetCodes, userCode);
            ra.addFlashAttribute("message", "Retrieval complete!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/viewRequest";
    }

    @GetMapping("/warehouse/process")
    public String viewProcess(@RequestParam Long requestId, Model model) {
        // 1. Chuyển trạng thái sang IN_PROGRESS
        warehouseRecoverUsecase.prepareProcessing(requestId);

        // 2. Lấy "gói dữ liệu" DTO
        RecoverProcessDTOResponse data = warehouseRecoverUsecase.getRecoverProcessData(requestId);

        // 3. Đưa vào model (Dùng tên "request" để bạn không phải sửa quá nhiều code HTML cũ)
        model.addAttribute("request", data);

        return "warehouse_recover_detail";
    }

    @PostMapping("/warehouse/confirm-item")
    public String confirmItem(@RequestParam Long detailId,
                              @RequestParam Long requestId,
                              RedirectAttributes ra) {
        try {
            // Chỉ gọi UseCase xử lý nghiệp vụ thu hồi và đóng request
            warehouseRecoverUsecase.executeRecovery(detailId, requestId);

            ra.addFlashAttribute("message", "Confirm Retrieval Asset done!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error Retrival: " + e.getMessage());
        }

        return "redirect:/recover-asset/warehouse/process?requestId=" + requestId;
    }
}

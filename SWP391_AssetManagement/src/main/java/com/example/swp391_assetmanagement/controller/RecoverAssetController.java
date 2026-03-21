package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.ViewAssetByUserDisabledDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAssetByUserDisabledDTOResponse;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.usecase.ExecuteRecoverUsecase;
import com.example.swp391_assetmanagement.usecase.ViewAssetByUserDisabledUsecase;
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

    private final ViewAssetByUserDisabledUsecase viewAssetByUserDisabledUsecase;
    private final ExecuteRecoverUsecase executeRecoverUsecase;
    private final WarehouseRecoverUsecase warehouseRecoverUsecase;
    private final AssetRequestService assetRequestService;

    @GetMapping("/manager/recoverAsset")
    public String viewUser(@ModelAttribute ViewAssetByUserDisabledDTORequest request, HttpSession session, Model model) {

        ViewAssetByUserDisabledDTOResponse response = viewAssetByUserDisabledUsecase.viewAssetDisabled(request,session);
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
            executeRecoverUsecase.execute(assetCodes, userCode);
            ra.addFlashAttribute("message", "Thu hồi tài sản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/viewRequest";
    }

    @GetMapping("/warehouse/process")
    public String viewProcess(@RequestParam Long requestId, Model model) {
        // 1. Chuyển trạng thái sang IN_PROGRESS (05) nếu Request đang ở mức APPROVED (03)
        warehouseRecoverUsecase.prepareProcessing(requestId);

        // 2. Lấy dữ liệu thực thể để hiển thị lên Form (Dùng Service)
        AssetRequest request = assetRequestService.findById(requestId);
        List<AssetInternalRequestDetail> details = assetRequestService.findDetailsByRequestId(requestId);

        model.addAttribute("request", request);
        model.addAttribute("details", details);

        return "warehouse_recover_detail"; // Trả về file HTML giao diện xử lý của Warehouse
    }

    @PostMapping("/warehouse/confirm-item")
    public String confirmItem(@RequestParam Long detailId,
                              @RequestParam Long requestId,
                              RedirectAttributes ra) {
        try {
            // Gọi UseCase thực hiện quy trình:
            // Update detail (isDone) -> Update Asset (Clear User, Status 08) -> Check hoàn tất Request tổng
            warehouseRecoverUsecase.executeRecovery(detailId, requestId);

            ra.addFlashAttribute("message", "Xác nhận thu hồi tài sản thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi xử lý thu hồi: " + e.getMessage());
        }

        // Quay lại trang danh sách chi tiết để tiếp tục xử lý các món khác
        return "redirect:/recover-asset/warehouse/process?requestId=" + requestId;
    }
}

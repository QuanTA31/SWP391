package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.AllocationDTORequest;
import com.example.swp391_assetmanagement.dto.request.AssetTypeDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AllocationService;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.usecase.ApproveAllocationRequestUsecase;
import com.example.swp391_assetmanagement.usecase.ConfirmAllocationReceiptUsecase;
import com.example.swp391_assetmanagement.usecase.CreateAllocationRequestUsecase;
import com.example.swp391_assetmanagement.usecase.ProcessAllocationAssignmentUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/allocation")
@RequiredArgsConstructor
public class AllocationController {

    private final CreateAllocationRequestUsecase createAllocationRequestUsecase;
    private final AllocationService allocationService;
    private final ApproveAllocationRequestUsecase approveAllocationRequestUsecase;
    private final AssetService assetService;
    private final ProcessAllocationAssignmentUsecase processAllocationAssignmentUsecase;
    private final ConfirmAllocationReceiptUsecase confirmAllocationReceiptUsecase;

    @GetMapping("/department/create")
    public String showCreateForm(Model model, HttpSession session) {
        String locationId = (String) session.getAttribute("LOCATION_ID");
        AllocationDTORequest dto = AllocationDTORequest.builder()
                .locationId(locationId)
                .build();
        model.addAttribute("allocationRequest", dto);
        model.addAttribute("isReadOnly", false);
        model.addAttribute("canApprove", false);
        model.addAttribute("requestStatusId", "01"); // DRAFT
        model.addAttribute("statusMessage", null);

        String locationName = "N/A";
        if (locationId != null && Location.hasValue(locationId)) {
            locationName = Location.of(locationId).getName();
        }
        model.addAttribute("locationName", locationName);

        populateEnums(model);
        return "NewAllocationRequest";
    }

    @GetMapping("/department/edit")
    public String showEditForm(@RequestParam Long id, Model model, HttpSession session) {
        AssetRequest req = allocationService.getAssetRequestById(id).orElse(null);
        if (req == null) return "redirect:/viewRequest";

        AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(id);
        if (detail == null) {
            detail = new AssetInternalRequestDetail();
            detail.assetRequestId = id;
        }

        // Map back to DTO
        List<Assets> assignedAssets = new java.util.ArrayList<>();
        if (detail.note != null && detail.note.startsWith("ASSIGNED_ASSETS:")) {
            String csv = detail.note.substring("ASSIGNED_ASSETS:".length());
            String[] ids = csv.split(",");
            for (String assetIdStr : ids) {
                try {
                    Long aid = Long.parseLong(assetIdStr.trim());
                    Assets asset = assetService.findById(aid);
                    if (asset != null) assignedAssets.add(asset);
                } catch (NumberFormatException ignored) {}
            }
        }

        AllocationDTORequest.AllocationDTORequestBuilder dtoBuilder = AllocationDTORequest.builder()
                .assetRequestId(id)
                .assetId(detail.assetId)
                .assetTypeId(detail.assetTypeId)
                .reason((detail.note != null && !detail.note.startsWith("ASSIGNED_ASSETS:")) ? detail.note : req.note)
                .action("draft")
                .locationId(detail.toLocationId)
                .quantity(detail.quantity != null ? detail.quantity : 0)
                .assignedAssets(assignedAssets);

        boolean isReadOnlyVal = !RequestStatus.DRAFT.getValue().equals(req.requestStatusId);
        model.addAttribute("isReadOnly", isReadOnlyVal);

        String role = (String) session.getAttribute("ROLE");
        int roleInt = (role != null) ? Integer.parseInt(role) : -1;
        boolean isManager = (roleInt == 2);
        
        String statusMessage = null;
        if (RequestStatus.APPROVED.getValue().equals(req.requestStatusId)) {
            statusMessage = "Yêu cầu này đã được duyệt.";
        } else if (RequestStatus.CANCELLED.getValue().equals(req.requestStatusId)) {
            statusMessage = "Yêu cầu này đã bị hủy bỏ/từ chối.";
        } else if (RequestStatus.IN_PROGRESS.getValue().equals(req.requestStatusId)) {
            statusMessage = "Yêu cầu đang trong quá trình cấp phát.";
        }
        
        model.addAttribute("statusMessage", statusMessage);
        boolean isPending = RequestStatus.PENDING_APPROVAL.getValue().equals(req.requestStatusId);
        model.addAttribute("canApprove", isManager && isPending);
        model.addAttribute("canAllocate", false);
        
        model.addAttribute("requestStatusId", req.requestStatusId);
        model.addAttribute("allocationRequest", dtoBuilder.build());

        String locId = detail.toLocationId != null ? detail.toLocationId : (String) session.getAttribute("LOCATION_ID");
        String locationName = "N/A";
        if (locId != null && Location.hasValue(locId)) {
            locationName = Location.of(locId).getName();
        }
        model.addAttribute("locationName", locationName);
        
        populateEnums(model);
        return "NewAllocationRequest";
    }

    @PostMapping("/department/create")
    public String createAllocationRequest(
            @RequestParam(required = false) Long assetRequestId,
            @RequestParam String assetTypeId,
            @RequestParam(required = false) Long assetId,
            @RequestParam String locationId,
            @RequestParam Integer quantity,
            @RequestParam String reason,
            @RequestParam String action,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        AllocationDTORequest dto = AllocationDTORequest.builder()
                .assetRequestId(assetRequestId)
                .assetTypeId(assetTypeId)
                .assetId(assetId)
                .locationId(locationId)
                .quantity(quantity)
                .reason(reason)
                .action(action)
                .build();

        try {
            createAllocationRequestUsecase.execute(dto, session);
            String msg = "draft".equalsIgnoreCase(dto.getAction())
                    ? "Đã lưu nháp yêu cầu cấp phát."
                    : "Yêu cầu cấp phát đã được gửi duyệt.";
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return dto.getAssetRequestId() == null
                    ? "redirect:/allocation/department/create"
                    : "redirect:/allocation/department/edit?id=" + dto.getAssetRequestId();
        }
        return "redirect:/viewRequest";
    }

    @PostMapping("/manager/approve")
    public String approveRequest(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            approveAllocationRequestUsecase.approve(id, session);
            redirectAttributes.addFlashAttribute("successMessage", "Yêu cầu cấp phát đã được duyệt.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi duyệt yêu cầu: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    @PostMapping("/manager/reject")
    public String rejectRequest(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            approveAllocationRequestUsecase.reject(id, session);
            redirectAttributes.addFlashAttribute("successMessage", "Yêu cầu cấp phát đã bị từ chối.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi từ chối yêu cầu: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    @GetMapping("/process")
    public String showProcessForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        String role = (String) session.getAttribute("ROLE");
        // Robust role check (02/2 or 03/3)
        if (role == null) return "redirect:/viewRequest";
        int roleInt = Integer.parseInt(role);
        if (roleInt != 2 && roleInt != 3) {
            return "redirect:/viewRequest";
        }

        Optional<AssetRequest> req = allocationService.getAssetRequestById(id);
        if (req.isEmpty()) return "redirect:/viewRequest";

        AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(id);
        if (detail == null) {
            // If detail is missing but it's an allocation, try to create one or handle it
            // For now, redirect with message
            return "redirect:/viewRequest";
        }

        // Count assets already CONFIRMED by Department: those in note where locationId = toLocationId
        // (locationId is set to toLocationId when Department confirms receipt — not just when Manager assigns)
        int alreadyConfirmedCount = 0;
        if (detail.note != null && detail.note.startsWith("ASSIGNED_ASSETS:")) {
            String csv = detail.note.substring("ASSIGNED_ASSETS:".length());
            for (String part : csv.split(",")) {
                try {
                    Long aid = Long.parseLong(part.trim());
                    Assets a = assetService.findById(aid);
                    if (a != null && detail.toLocationId != null
                            && detail.toLocationId.equals(a.locationId)) {
                        alreadyConfirmedCount++;
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        // Separate the real reason from the ASSIGNED_ASSETS note encoding
        String requestNote = req.get().note;
        if (requestNote != null && requestNote.startsWith("ASSIGNED_ASSETS:")) {
            requestNote = "";
        }

        // Fetch Assets (available ones)
        List<Assets> stockAssets = new java.util.ArrayList<>(assetService.findByTypeAndStatus(detail.assetTypeId, "00")); // STOCK_IN
        List<Assets> recoveredAssets = new java.util.ArrayList<>(assetService.findByTypeAndStatus(detail.assetTypeId, "08")); // STOCKED
        List<Assets> transferringAssets = assetService.findByTypeAndStatus(detail.assetTypeId, "03"); // TRANSFERRING

        // Load all assets already assigned in this request (TRANSFERRING or ASSIGNED in previous rounds)
        // And place them into either the stock or recovered list so they appear in UI but are disabled
        List<Assets> alreadyAssignedAssets = new java.util.ArrayList<>();
        java.util.List<Long> alreadyAssignedIds = new java.util.ArrayList<>();
        if (detail.note != null && detail.note.startsWith("ASSIGNED_ASSETS:")) {
            String csv = detail.note.substring("ASSIGNED_ASSETS:".length());
            for (String part : csv.split(",")) {
                try {
                    Long aid = Long.parseLong(part.trim());
                    alreadyAssignedIds.add(aid);
                    Assets a = assetService.findById(aid);
                    if (a != null) {
                       alreadyAssignedAssets.add(a);
                       // We categorize them into stock or recovered based on a heuristic,
                       // or simply put them all in stock for simplicity so Manager sees them.
                       stockAssets.add(0, a); // Put at the top of the stock list
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        // Add transferring assets from OTHER requests to stockAssets so they appear as disabled
        if (transferringAssets != null) {
            for (Assets ta : transferringAssets) {
                if (!alreadyAssignedIds.contains(ta.id)) {
                    stockAssets.add(ta); // Show them in the list so manager knows they exist but are taken
                }
            }
        }

        model.addAttribute("request", req.get());
        model.addAttribute("detail", detail);
        model.addAttribute("requestNote", requestNote);
        model.addAttribute("alreadyConfirmedCount", alreadyConfirmedCount);
        model.addAttribute("alreadyAssignedIds", alreadyAssignedIds);
        model.addAttribute("stockAssets", stockAssets);
        model.addAttribute("recoveredAssets", recoveredAssets);
        
        return "ProcessingAllocation";
    }

    @PostMapping("/process")
    public String processAllocation(@RequestParam Long requestId,
                                    @RequestParam List<Long> selectedAssetIds,
                                    RedirectAttributes redirectAttributes) {
        try {
            processAllocationAssignmentUsecase.execute(requestId, selectedAssetIds);
            redirectAttributes.addFlashAttribute("successMessage", "Cấp phát tài sản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi cấp phát: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    @GetMapping("/department/receipt")
    public String showReceiptPage(@RequestParam Long requestId, Model model, HttpSession session) {
        AssetRequest req = allocationService.getAssetRequestById(requestId).orElse(null);
        if (req == null) return "redirect:/viewRequest";

        AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(requestId);
        if (detail == null) return "redirect:/viewRequest";

        // Guard: only show receipt page when Warehouse has dispatched (is_done = false explicitly)
        // If is_done is null → Warehouse hasn't dispatched yet → go to normal edit view
        // If is_done is true → already confirmed → go to normal edit view
        if (detail.isDone == null || Boolean.TRUE.equals(detail.isDone)) {
            return "redirect:/allocation/department/edit?id=" + requestId;
        }

        // Parse assigned asset IDs from note field
        List<Assets> assignedAssets = new java.util.ArrayList<>();
        if (detail.note != null && detail.note.startsWith("ASSIGNED_ASSETS:")) {
            String csv = detail.note.substring("ASSIGNED_ASSETS:".length());
            for (String part : csv.split(",")) {
                try {
                    Long aid = Long.parseLong(part.trim());
                    Assets asset = assetService.findById(aid);
                    if (asset != null) assignedAssets.add(asset);
                } catch (NumberFormatException ignored) {}
            }
        }

        model.addAttribute("request", req);
        model.addAttribute("detail", detail);
        model.addAttribute("assignedAssets", assignedAssets);
        return "ConfirmAllocationReceipt";
    }

    @PostMapping("/department/confirm-receipt")
    public String confirmReceipt(@RequestParam Long requestId,
                                 RedirectAttributes redirectAttributes) {
        try {
            confirmAllocationReceiptUsecase.execute(requestId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xác nhận nhận tài sản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi xác nhận: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    // ===== WAREHOUSE ENDPOINTS =====

    @GetMapping("/warehouse/view")
    public String showWarehouseView(@RequestParam Long requestId, Model model) {
        AssetRequest req = allocationService.getAssetRequestById(requestId).orElse(null);
        if (req == null) return "redirect:/viewRequest";

        AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(requestId);
        if (detail == null) return "redirect:/viewRequest";

        // Parse assets assigned by Manager
        List<Assets> assignedAssets = new java.util.ArrayList<>();
        if (detail.note != null && detail.note.startsWith("ASSIGNED_ASSETS:")) {
            String csv = detail.note.substring("ASSIGNED_ASSETS:".length());
            for (String part : csv.split(",")) {
                try {
                    Long aid = Long.parseLong(part.trim());
                    Assets asset = assetService.findById(aid);
                    if (asset != null) assignedAssets.add(asset);
                } catch (NumberFormatException ignored) {}
            }
        }

        // Show dispatch button only if not yet dispatched (is_done == null)
        boolean canDispatch = (detail.isDone == null);

        model.addAttribute("request", req);
        model.addAttribute("detail", detail);
        model.addAttribute("assignedAssets", assignedAssets);
        model.addAttribute("canDispatch", canDispatch);
        return "WarehouseAllocationView";
    }

    @PostMapping("/warehouse/dispatch")
    public String warehouseDispatch(@RequestParam Long requestId,
                                    RedirectAttributes redirectAttributes) {
        try {
            AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(requestId);
            if (detail == null) throw new RuntimeException("Không tìm thấy chi tiết yêu cầu.");
            detail.isDone = false; // Mark as dispatched by Warehouse
            allocationService.updateIsDone(detail);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xác nhận cấp phát! Phòng ban sẽ nhận tài sản.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    private void populateEnums(Model model) {
        model.addAttribute("assetTypes",
                Arrays.stream(AssetType.values())
                        .map(a -> AssetTypeDTORequest.builder()
                                .value(a.getValue())
                                .label(a.getName())
                                .build())
                        .toList());
        model.addAttribute("locations",
                Arrays.stream(Location.values())
                        .map(l -> AssetTypeDTORequest.builder()
                                .value(l.getValue())
                                .label(l.getName())
                                .build())
                        .toList());
    }
}

package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.AllocationDTORequest;
import com.example.swp391_assetmanagement.dto.request.AssetTypeDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AllocationService;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.serviceresponse.UserDropdownResponse;
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

import java.util.*;

@Controller
@RequestMapping("/allocation")
@RequiredArgsConstructor
public class AllocationController {

    private final CreateAllocationRequestUsecase createAllocationRequestUsecase;
    private final AllocationService allocationService;
    private final ApproveAllocationRequestUsecase approveAllocationRequestUsecase;
    private final AssetService assetService;
    private final UserService userService;
    private final ProcessAllocationAssignmentUsecase processAllocationAssignmentUsecase;
    private final ConfirmAllocationReceiptUsecase confirmAllocationReceiptUsecase;

    //1
    //select 1 list từ bảng assetInternalDetail theo assetRequestID
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

        List<UserDropdownResponse> departmentUsers = new ArrayList<>();
        if (locationId != null) {
            departmentUsers = userService.getActiveUsersByLocation(locationId);
        }
        model.addAttribute("departmentUsers", departmentUsers);

        populateEnums(model);
        return "NewAllocationRequest";
    }

    //3 : manager approval
    @GetMapping("/department/edit")
    public String showEditForm(@RequestParam Long id, Model model, HttpSession session) {
        AssetRequest req = allocationService.getAssetRequestById(id).orElse(null);
        if (req == null) return "redirect:/viewRequest";

        // Load ALL detail records for this request (N records = N units requested)
        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(id);

        // Use the first record to get type/location info; quantity = number of records
        AssetInternalRequestDetail firstDetail = details.isEmpty() ? null : details.get(0);

        String assetTypeId = Objects.nonNull(firstDetail)
                ? firstDetail.assetTypeId
                : null;

        String toLocationId = Objects.nonNull(firstDetail)
                ? firstDetail.toLocationId
                : null;

        Long toUserId = Objects.nonNull(firstDetail)
                ? firstDetail.toUserId
                : null;

        int quantity = details.size(); // 1 record per unit

        // Collect assets already assigned and RECEIVED by department
        List<Assets> assignedAssets = new ArrayList<>();
        for (AssetInternalRequestDetail d : details) {
            // Only add to assignedAssets if department has confirmed receipt (isDone == true)
            // This prevents the department from seeing assets prematurely when the manager just selected them.
            if (d.assetId != null && Boolean.TRUE.equals(d.isDone)) {
                Assets asset = assetService.findById(d.assetId);
                if (asset != null) assignedAssets.add(asset);
            }
        }

        AllocationDTORequest dto = AllocationDTORequest.builder()
                .assetRequestId(id)
                .assetTypeId(assetTypeId)
                .reason(req.note)
                .action("draft")
                .locationId(toLocationId)
                .quantity(quantity)
                .assignedAssets(assignedAssets)
                .toUserId(toUserId)
                .build();

        boolean isReadOnlyVal = !RequestStatus.DRAFT.getValue().equals(req.requestStatusId);
        model.addAttribute("isReadOnly", isReadOnlyVal);

        String role = (String) session.getAttribute("ROLE");
        int roleInt = (role != null) ? Integer.parseInt(role) : -1;
        boolean isManager = (roleInt == 2);

        String statusMessage = null;
        if (RequestStatus.APPROVED.getValue().equals(req.requestStatusId)) {
            statusMessage = "This request has been approved.";
        } else if (RequestStatus.CANCELLED.getValue().equals(req.requestStatusId)) {
            statusMessage = "This request has been cancelled/rejected.";
        } else if (RequestStatus.IN_PROGRESS.getValue().equals(req.requestStatusId)) {
            statusMessage = "This request is currently being allocated.";
        }

        model.addAttribute("statusMessage", statusMessage);
        boolean isPending = RequestStatus.PENDING_APPROVAL.getValue().equals(req.requestStatusId);
        model.addAttribute("canApprove", isManager && isPending);
        model.addAttribute("canAllocate", false);

        model.addAttribute("requestStatusId", req.requestStatusId);
        model.addAttribute("allocationRequest", dto);

        String locId = toLocationId != null ? toLocationId : (String) session.getAttribute("LOCATION_ID");
        String locationName = "N/A";
        if (locId != null && Location.hasValue(locId)) {
            locationName = Location.of(locId).getName();
        }
        model.addAttribute("locationName", locationName);

        List<UserDropdownResponse> departmentUsers = new ArrayList<>();
        if (locId != null) {
            departmentUsers = userService.getActiveUsersByLocation(locId);
        }
        model.addAttribute("departmentUsers", departmentUsers);

        populateEnums(model);
        return "NewAllocationRequest";
    }

    //2 : draf, submit
    @PostMapping("/department/create")
    public String createAllocationRequest(
            @RequestParam(required = false) Long assetRequestId,
            @RequestParam(required = false) String assetTypeId,
            @RequestParam(required = false) Long assetId,
            @RequestParam(required = false) String locationId,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String reason,
            @RequestParam(required = false) Long toUserId,
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
                .toUserId(toUserId)
                .build();

        try {
            createAllocationRequestUsecase.execute(dto, session);
            String msg = "draft".equalsIgnoreCase(dto.getAction())
                    ? "Allocation request saved as draft."
                    : "Allocation request submitted for approval.";
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return dto.getAssetRequestId() == null
                    ? "redirect:/allocation/department/create"
                    : "redirect:/allocation/department/edit?id=" + dto.getAssetRequestId();
        }
        return "redirect:/viewRequest";
    }

    //4: manager approval, cancael
    @PostMapping("/manager/approve")
    public String approveRequest(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            approveAllocationRequestUsecase.approve(id, session);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation request approved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error approving request: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    @PostMapping("/manager/reject")
    public String rejectRequest(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            approveAllocationRequestUsecase.reject(id, session);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation request rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting request: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    // 6: view tài sản để cấp phát
    @GetMapping("/process")
    public String showProcessForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        String role = (String) session.getAttribute("ROLE");
        if (role == null) return "redirect:/viewRequest";
        int roleInt = Integer.parseInt(role);
        if (roleInt != 2 && roleInt != 3) {
            return "redirect:/viewRequest";
        }

        Optional<AssetRequest> req = allocationService.getAssetRequestById(id);
        if (req.isEmpty()) return "redirect:/viewRequest";

        // Load all N detail records for this request
        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(id);
        if (details.isEmpty()) return "redirect:/viewRequest";

        AssetInternalRequestDetail firstDetail = details.get(0);
        String assetTypeId = firstDetail.assetTypeId;
        String toLocationId = firstDetail.toLocationId;

        // Count assets already confirmed by department (asset_id set + location transferred)
        int alreadyConfirmedCount = 0;
        List<Long> alreadyAssignedIds = new ArrayList<>();
        List<Long> alreadyReceivedIds = new ArrayList<>();
        List<Assets> alreadyAssignedAssets = new ArrayList<>();

        for (AssetInternalRequestDetail d : details) {
            if (d.assetId != null) {
                alreadyAssignedIds.add(d.assetId);
                Assets a = assetService.findById(d.assetId);
                if (a != null) {
                    alreadyAssignedAssets.add(a);
                    // Dùng cờ isDone == true để biết chính xác Department đã nhận thay vì check locationId
                    // Vì nếu tài sản tình cờ đang ở cùng location đích thì nó sẽ bị dính lỗi hiển thị sớm.
                    if (Boolean.TRUE.equals(d.isDone)) {
                        alreadyConfirmedCount++;
                        alreadyReceivedIds.add(d.assetId);
                    }
                }
            }
        }

        String requestNote = req.get().note;

        Long toUserId = firstDetail.toUserId;
        String toUserName = "Not specified";
        if (toUserId != null) {
            String uName = userService.getUserNameById(toUserId);
            if (uName != null) {
                toUserName = uName;
            }
        }

        // Fetch available assets
        // Stock: status 01 (NEW) or 08 (STOCKED)
        List<Assets> stockAssets = new ArrayList<>(assetService.findStockByType(assetTypeId));
        // Recovered: status 02 (ASSIGNED) but user is SUSPENDED(02) or DISABLED(03)
        List<Assets> recoveredAssets = new ArrayList<>(assetService.findRecoveredByType(assetTypeId));

        // Put already-assigned assets at top of stock list so manager sees them (disabled)
        for (Assets a : alreadyAssignedAssets) {
            stockAssets.add(0, a);
        }

        model.addAttribute("request", req.get());
        model.addAttribute("detail", firstDetail);
        model.addAttribute("requestedQty", details.size()); // total N units requested
        model.addAttribute("requestNote", requestNote);
        model.addAttribute("alreadyConfirmedCount", alreadyConfirmedCount);
        model.addAttribute("alreadyAssignedIds", alreadyAssignedIds);
        model.addAttribute("alreadyReceivedIds", alreadyReceivedIds);
        model.addAttribute("stockAssets", stockAssets);
        model.addAttribute("recoveredAssets", recoveredAssets);
        model.addAttribute("toUserName", toUserName);

        return "ProcessingAllocation";
    }

    // 7: tạo tài sản cấp phát
    @PostMapping("/process")
    public String processAllocation(@RequestParam Long requestId,
                                    @RequestParam List<Long> selectedAssetIds,
                                    RedirectAttributes redirectAttributes) {
        try {
            processAllocationAssignmentUsecase.execute(requestId, selectedAssetIds);
            redirectAttributes.addFlashAttribute("successMessage", "Asset allocated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Allocation error: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    //11 : get ra thông tin asset để xác nhận
    @GetMapping("/department/receipt")
    public String showReceiptPage(@RequestParam Long requestId, Model model, HttpSession session) {
        AssetRequest req = allocationService.getAssetRequestById(requestId).orElse(null);
        if (req == null) return "redirect:/viewRequest";

        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
        if (details.isEmpty()) return "redirect:/viewRequest";

        AssetInternalRequestDetail firstDetail = details.get(0);

        // Guard: only show receipt page when Warehouse has dispatched (is_done = false explicitly)
        // Check the first detail; in the N-records model all share the same dispatch state
        if (firstDetail.isDone == null || Boolean.TRUE.equals(firstDetail.isDone)) {
            return "redirect:/allocation/department/edit?id=" + requestId;
        }

        // Collect assigned assets from detail records (asset_id field)
        List<Assets> assignedAssets = new java.util.ArrayList<>();
        for (AssetInternalRequestDetail d : details) {
            if (d.assetId != null) {
                Assets asset = assetService.findById(d.assetId);
                if (asset != null) assignedAssets.add(asset);
            }
        }

        model.addAttribute("request", req);
        model.addAttribute("detail", firstDetail);
        model.addAttribute("requestedQty", details.size()); // total N units originally requested
        model.addAttribute("assignedAssets", assignedAssets);

        Long toUserId = firstDetail.toUserId;
        String toUserName = "Not specified";
        if (toUserId != null) {
            String uName = userService.getUserNameById(toUserId);
            if (uName != null) {
                toUserName = uName;
            }
        }
        model.addAttribute("toUserName", toUserName);

        return "ConfirmAllocationReceipt";
    }

    // 13: confirm đã nhận
    @PostMapping("/department/confirm-receipt")
    public String confirmReceipt(@RequestParam Long requestId,
                                 RedirectAttributes redirectAttributes) {
        try {
            confirmAllocationReceiptUsecase.execute(requestId);
            redirectAttributes.addFlashAttribute("successMessage", "Asset receipt confirmed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Confirmation error: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    // ===== WAREHOUSE ENDPOINTS =====
    // 8: kho cấp phát theo detail id
    @GetMapping("/warehouse/view")
    public String showWarehouseView(@RequestParam Long requestId, Model model) {
        AssetRequest req = allocationService.getAssetRequestById(requestId).orElse(null);
        if (req == null) return "redirect:/viewRequest";

        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
        if (details.isEmpty()) return "redirect:/viewRequest";

        AssetInternalRequestDetail firstDetail = details.get(0);

        // Collect assigned assets from detail records (asset_id field)
        List<Assets> warehouseAssets = new java.util.ArrayList<>();
        for (AssetInternalRequestDetail d : details) {
            if (d.assetId != null) {
                Assets asset = assetService.findById(d.assetId);
                if (asset != null) warehouseAssets.add(asset);
            }
        }

        // Show dispatch button if ANY newly-assigned detail (assetId set, not yet dispatched)
        boolean canDispatch = details.stream()
                .anyMatch(d -> d.assetId != null && d.isDone == null);

        model.addAttribute("request", req);
        model.addAttribute("detail", firstDetail);
        model.addAttribute("requestedQty", details.size()); // total N units originally requested
        model.addAttribute("warehouseAssets", warehouseAssets);
        model.addAttribute("canDispatch", canDispatch);

        Long toUserId = firstDetail.toUserId;
        String toUserName = "Not specified";
        if (toUserId != null) {
            String uName = userService.getUserNameById(toUserId);
            if (uName != null) {
                toUserName = uName;
            }
        }
        model.addAttribute("toUserName", toUserName);

        return "WarehouseAllocationView";
    }

    // 9: kho cấp phất tài sản
    @PostMapping("/warehouse/dispatch")
    public String warehouseDispatch(@RequestParam Long requestId,
                                    RedirectAttributes redirectAttributes) {
        try {
            List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
            if (details.isEmpty()) throw new RuntimeException("Request detail not found.");
            // Mark all detail records as dispatched (is_done = false = warehouse has sent)
            for (AssetInternalRequestDetail d : details) {
                d.isDone = false;
                allocationService.updateIsDone(d);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Dispatch confirmed! Department will receive the assets.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
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

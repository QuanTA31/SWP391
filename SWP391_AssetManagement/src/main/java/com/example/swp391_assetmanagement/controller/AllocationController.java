package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.dto.request.AllocationDTORequest;
import com.example.swp391_assetmanagement.dto.request.AssetTypeDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.common.RoleChecker;
import com.example.swp391_assetmanagement.service.AllocationService;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.serviceresponse.UserDropdownResponse;
import com.example.swp391_assetmanagement.usecase.ApproveAllocationRequestUsecase;
import com.example.swp391_assetmanagement.usecase.ConfirmAllocationReceiptUsecase;
import com.example.swp391_assetmanagement.usecase.CreateAllocationRequestUsecase;
import com.example.swp391_assetmanagement.usecase.ProcessAllocationAssignmentUsecase;
import com.example.swp391_assetmanagement.usecase.GetAllocationSummaryUsecase;
import com.example.swp391_assetmanagement.dto.response.AllocationSummaryResponse;
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
    private final GetAllocationSummaryUsecase getAllocationSummaryUsecase;
    private final RoleChecker roleChecker;

    //1. Create Allocaiton Request Form
    @GetMapping("/department/create")
    public String showCreateForm(Model model, HttpSession session) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.DEPARTMENT_MANAGER);
        String locationId = (String) session.getAttribute("LOCATION_ID");

        AllocationDTORequest dto = AllocationDTORequest.builder()
                .locationId(locationId)
                .build();
        model.addAttribute("allocationRequest", dto);
        model.addAttribute("isReadOnly", false);
        model.addAttribute("canApprove", false);
        model.addAttribute("requestStatusId", "01"); // DRAFT
        model.addAttribute("statusMessage", null);

        //  Hiển thị tên địa điểm với người dùng.
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
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.DEPARTMENT_MANAGER );

        // Đóng gói tất cả thông tin lẻ tẻ từ form vào một đối tượng duy nhất
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

    //3: manager approval, cancael
    @PostMapping("/manager/approve")
    public String approveRequest(@RequestParam("id") Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.MANAGER );
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
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.MANAGER );
        try {
            approveAllocationRequestUsecase.reject(id, session);
            redirectAttributes.addFlashAttribute("successMessage", "Allocation request rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting request: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    //4 : view detail request
    @GetMapping("/department/edit")
    public String showEditForm(@RequestParam Long id, Model model, HttpSession session) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.DEPARTMENT_MANAGER, Roles.MANAGER );

        //Lấy thông tin chung của yêu cầu từ bảng asset_request
        AssetRequest req = allocationService.getAssetRequestById(id).orElse(null);
        if (req == null) return "redirect:/viewRequest";
        // Lấy danh sách chi tiết để biết chính xác số lượng cần cấp phát và loại tài sản
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

        //Receipt
        // Lọc danh sách Tài sản đã thực sự nhận
        List<Assets> assignedAssets = new ArrayList<>();
        for (AssetInternalRequestDetail d : details) {
            // Only add to assignedAssets if department has confirmed receipt
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

        // Nếu trạng thái khác DRAFT thì khóa các ô nhập liệu
        boolean isReadOnlyVal = !RequestStatus.DRAFT.getValue().equals(req.requestStatusId);
        model.addAttribute("isReadOnly", isReadOnlyVal);

        String role = (String) session.getAttribute("ROLE");
        int roleInt = (role != null) ? Integer.parseInt(role) : -1;
        boolean isManager = (roleInt == 2);

        // Hiển thị thông báo tương ứng với trạng thái (APPROVED, CANCELLED, IN_PROGRESS)
        String statusMessage = null;
        if (RequestStatus.APPROVED.getValue().equals(req.requestStatusId)) {
            statusMessage = "This request has been approved.";
        } else if (RequestStatus.CANCELLED.getValue().equals(req.requestStatusId)) {
            statusMessage = "This request has been cancelled/rejected.";
        } else if (RequestStatus.IN_PROGRESS.getValue().equals(req.requestStatusId)) {
            statusMessage = "This request is currently being allocated.";
        }
        model.addAttribute("statusMessage", statusMessage);

        // Phê duyệt
        // Kiểm tra xem Manager có quyền Duyệt đơn này không
        // Nút duyệt chỉ hiện ra khi đồng thời: Manager và Pending
        boolean isPending = RequestStatus.PENDING_APPROVAL.getValue().equals(req.requestStatusId);
        model.addAttribute("canApprove", isManager && isPending);
        model.addAttribute("canAllocate", false);

        model.addAttribute("requestStatusId", req.requestStatusId);
        model.addAttribute("allocationRequest", dto);

        // Lấy danh sách nhân viên thuộc phòng ban (Location) đó để hiện lên ô chọn "Người nhận" trên giao diện.
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

    // 5: view tài sản để cấp phát
    @GetMapping("/process")
    public String showProcessForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.MANAGER);
        String role = (String) session.getAttribute("ROLE");
        if (role == null) return "redirect:/viewRequest";
        int roleInt = Integer.parseInt(role);
        if (roleInt != 2 && roleInt != 3) {
            return "redirect:/viewRequest";
        }

        // Lấy thông tin tổng thể của yêu cầu
        Optional<AssetRequest> req = allocationService.getAssetRequestById(id);
        if (req.isEmpty()) return "redirect:/viewRequest";

        // Lấy danh sách chi tiết
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
                Assets a = assetService.findById(d.assetId); // lấy chi tiết tài sản
                if (a != null) {
                    alreadyAssignedAssets.add(a);
                    // theo dõi "Số lượng đã thực nhận"
                    // theo dõi chính xác Department đã nhận
                    if (Boolean.TRUE.equals(d.isDone)) {
                        alreadyConfirmedCount++;
                        alreadyReceivedIds.add(d.assetId);
                    }
                }
            }
        }

        String requestNote = req.get().note;

        // Lấy tên đầy đủ của người sẽ nhận tài sản
        Long toUserId = firstDetail.toUserId;
        String toUserName = "Not specified";
        if (toUserId != null) {
            String uName = userService.getUserNameById(toUserId);
            if (uName != null) {
                toUserName = uName;
            }
        }

        // tìm available assets
        // Tài sản trong kho: Trạng thái 01 (Mới) hoặc 08 (Trong kho)
        List<Assets> stockAssets = new ArrayList<>(assetService.findStockByType(assetTypeId));
        // Tài sản thu hồi: Trạng thái 02 (Đã cấp) nhưng User sở hữu đang bị SUSPENDED hoặc DISABLED
        List<Assets> recoveredAssets = new ArrayList<>(assetService.findRecoveredByType(assetTypeId));

        //Những tài sản đã được gán trước đó sẽ được "đưa lên đầu" danh sách kho.
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

    // 6: tạo tài sản cấp phát
    @PostMapping("/process")
    public String processAllocation(@RequestParam Long requestId,
                                    @RequestParam List<Long> selectedAssetIds,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.MANAGER);
        try {
            processAllocationAssignmentUsecase.execute(requestId, selectedAssetIds);
            redirectAttributes.addFlashAttribute("successMessage", "Asset allocated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Allocation error: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    // ===== WAREHOUSE ENDPOINTS =====
    // 7: kho cấp phát theo detail id
    @GetMapping("/warehouse/view")
    public String showWarehouseView(@RequestParam Long requestId, Model model, HttpSession session) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.WAREHOUSE );

        // lấy thông tin đơn hàng và danh sách các dòng chi tiết
        AssetRequest req = allocationService.getAssetRequestById(requestId).orElse(null);
        if (req == null) return "redirect:/viewRequest";
        // Lấy danh sách chi tiết để biết chính xác số lượng cần cấp phát và loại tài sản
        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
        if (details.isEmpty()) return "redirect:/viewRequest";

        AssetInternalRequestDetail firstDetail = details.get(0);

        // Tìm kiếm những tài sản đã được chỉ định
        List<Assets> warehouseAssets = new ArrayList<>();
        for (AssetInternalRequestDetail d : details) {
            if (d.assetId != null) {
                Assets asset = assetService.findById(d.assetId);
                if (asset != null) warehouseAssets.add(asset);
            }
        }

        // Show dispatch button if đồng thời: có ít nhất 1 record được gán và tài sản đó chưa xasc nhận hoàn thành
        boolean canDispatch = details.stream()
                .anyMatch(d -> d.assetId != null && d.isDone == null);

        model.addAttribute("request", req);
        model.addAttribute("detail", firstDetail);
        model.addAttribute("requestedQty", details.size()); // total N units originally requested
        model.addAttribute("warehouseAssets", warehouseAssets);
        model.addAttribute("canDispatch", canDispatch);

        // Lấy tên đầy đủ của người sẽ nhận tài sản
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

    // 8: kho cấp phất tài sản
    @PostMapping("/warehouse/dispatch")
    public String warehouseDispatch(@RequestParam Long requestId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.WAREHOUSE );

        try {
            // Lấy ra toàn bộ danh sách các thiết bị cụ thể thuộc đơn hàng này từ database.
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

    //9 : get ra thông tin asset để xác nhận
    @GetMapping("/department/receipt")
    public String showReceiptPage(@RequestParam Long requestId, Model model, HttpSession session) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.DEPARTMENT_MANAGER );

        // lấy thông tin đơn hàng và danh sách các dòng chi tiết
        AssetRequest req = allocationService.getAssetRequestById(requestId).orElse(null);
        if (req == null) return "redirect:/viewRequest";
        // Lấy danh sách chi tiết để biết chính xác số lượng cần cấp phát và loại tài sản
        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
        if (details.isEmpty()) return "redirect:/viewRequest";

        AssetInternalRequestDetail firstDetail = details.get(0);

        // Guard: only show receipt page when Warehouse has dispatched (is_done = false explicitly)
        //Trang "Xác nhận nhận hàng" này chỉ được phép hiển thị khi hàng Đang trên đường giao.
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

        // Lấy tên đầy đủ của người sẽ nhận tài sản
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

    // 10: confirm đã nhận
    @PostMapping("/department/confirm-receipt")
    public String confirmReceipt(@RequestParam Long requestId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.DEPARTMENT_MANAGER );
        try {
            confirmAllocationReceiptUsecase.execute(requestId);
            redirectAttributes.addFlashAttribute("successMessage", "Asset receipt confirmed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Confirmation error: " + e.getMessage());
        }
        return "redirect:/viewRequest";
    }

    //chuyển đổi dữ liệu từ các Enum (là các giá trị cố định trong code Java) thành danh sách các đối tượng DTO
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


    @GetMapping("/department/summary")
    public String showSummary(Model model, HttpSession session) {
        roleChecker.requireRole((String) session.getAttribute("USER_CODE"), Roles.DEPARTMENT_MANAGER );
        AllocationSummaryResponse summary = getAllocationSummaryUsecase.execute(session);
        model.addAttribute("summary", summary);
        return "AllocationSummary";
    }
    
}

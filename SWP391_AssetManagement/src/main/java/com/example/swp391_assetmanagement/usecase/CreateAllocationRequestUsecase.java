package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.AllocationDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AllocationService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateAllocationRequestUsecase {

    private final AllocationService allocationService;
    private final UserService userService;

    @Transactional
    public void execute(AllocationDTORequest dto, HttpSession session) {

        // Validate required fields when creating (not draft)
        if (!"draft".equalsIgnoreCase(dto.getAction())) {
            if (dto.getAssetTypeId() == null || dto.getAssetTypeId().trim().isEmpty()) {
                throw new IllegalArgumentException("Please select an asset type.");
            }
            if (dto.getQuantity() == null || dto.getQuantity() < 1) {
                throw new IllegalArgumentException("Please enter a quantity (minimum 1).");
            }
            if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Please enter a reason for the request.");
            }
        }

        // Xác định trạng thái của request
        String statusId = "draft".equalsIgnoreCase(dto.getAction())
                ? RequestStatus.DRAFT.getValue()
                : RequestStatus.PENDING_APPROVAL.getValue();

        String requesterCode = (String) session.getAttribute("USER_CODE");
        Long requestedBy = userService.getIdByUserCode(requesterCode);

        if (dto.getAssetRequestId() == null) {
            // == CREATE NEW ==
            AssetRequest assetRequest = new AssetRequest();
            assetRequest.requestTypeId = RequestType.ALLOCATION.getValue();
            assetRequest.requestStatusId = statusId;
            assetRequest.requestedBy = requestedBy;
            assetRequest.requestedDate = LocalDate.now();
            assetRequest.note = dto.getReason();
            assetRequest.createdAt = LocalDateTime.now();

            Long assetRequestId = allocationService.createAssetRequest(assetRequest);

            // Insert N records into asset_internal_request_detail (1 record per unit)
            List<AssetInternalRequestDetail> details = buildDetailRecords(dto, assetRequestId);
            allocationService.batchCreateInternalDetails(details);

        } else {
            // == UPDATE EXISTING DRAFT ==
            AssetRequest assetRequest = allocationService.getAssetRequestById(dto.getAssetRequestId()).orElse(null);
            if (assetRequest == null) throw new IllegalArgumentException("Request not found");

            assetRequest.requestStatusId = statusId;
            assetRequest.note = dto.getReason();
            allocationService.updateAssetRequest(assetRequest);

            // Delete old detail records and re-insert N records
            allocationService.deleteInternalDetailsByRequestId(dto.getAssetRequestId());
            List<AssetInternalRequestDetail> details = buildDetailRecords(dto, dto.getAssetRequestId());
            allocationService.batchCreateInternalDetails(details);
        }
    }

    /**
     * Builds N AssetInternalRequestDetail records (1 per unit requested).
     * Each record represents one asset slot with quantity = 1.
     * If quantity is null (draft), inserts 1 record as placeholder.
     */
    private List<AssetInternalRequestDetail> buildDetailRecords(AllocationDTORequest dto, Long assetRequestId) {
        int count = (dto.getQuantity() != null && dto.getQuantity() > 0) ? dto.getQuantity() : 1;
        List<AssetInternalRequestDetail> details = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            AssetInternalRequestDetail detail = new AssetInternalRequestDetail();
            detail.assetRequestId = assetRequestId;
            detail.assetTypeId = dto.getAssetTypeId();
            detail.toLocationId = dto.getLocationId();
            detail.toUserId = dto.getToUserId();
            detail.quantity = 1; // each record = 1 unit
            detail.note = dto.getReason();
            detail.createdAt = LocalDateTime.now();
            details.add(detail);
        }
        return details;
    }
}

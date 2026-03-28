package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AllocationService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmAllocationReceiptUsecase {

    private final AssetService assetService;
    private final AssetRequestService assetRequestService;
    private final AllocationService allocationService;

    @Transactional
    public void execute(Long requestId) {
        // 1. Load all detail records for this request (N records = N units)
        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
        if (details.isEmpty()) {
            throw new RuntimeException("Allocation details not found for Request ID: " + requestId);
        }

        // 2. Collect assigned asset IDs from detail records (asset_id field)
        // 3. Mark each assigned asset as ASSIGNED (status="02") and move to destination location & user
        List<Long> assignedAssetIds = new ArrayList<>();
        List<Assets> toUpdate = new ArrayList<>();

        for (AssetInternalRequestDetail d : details) {
            if (d.assetId != null) {
                assignedAssetIds.add(d.assetId);

                Assets asset = assetService.findById(d.assetId);
                if (asset != null) {
                    asset.assetStatusId = AssetStatus.ASSIGNED.getValue();
                    if (d.toLocationId != null) {
                        asset.locationId = d.toLocationId;
                    }
                    if (d.toUserId != null) {
                        asset.currentUserId = d.toUserId;
                    }
                    toUpdate.add(asset);
                }
            }
        }

        if (assignedAssetIds.isEmpty()) {
            throw new RuntimeException("No assets have been allocated in this request.");
        }

        if (!toUpdate.isEmpty()) {
            allocationService.batchUpdateAllocation(toUpdate);
        }

        // 4. Mark all assigned detail records as is_done = true (department confirmed receipt)
        for (AssetInternalRequestDetail d : details) {
            if (d.assetId != null) {
                d.isDone = true;
                allocationService.updateIsDone(d);
            }
        }

        // 5. Determine completion: assigned count vs total detail records
        int assignedCount = assignedAssetIds.size();        // Số máy đã bàn giao đợt này
        int totalRequested = details.size();                // Tổng số máy yêu cầu ban đầu

        if (assignedCount >= totalRequested) {
            // Fully fulfilled → COMPLETED
            AssetRequest request = allocationService.getAssetRequestById(requestId).orElse(null);
            if (request != null) {
                request.requestStatusId = RequestStatus.COMPLETED.getValue();
                request.handoverDate = LocalDate.now();
                assetRequestService.updatePurchaseRequestStatus(request);
            }
        }
        // Else: partially fulfilled → stay IN_PROGRESS, department can request more
    }
}

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

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmAllocationReceiptUsecase {

    //private final AssetInternalRequestDetailDAO assetInternalRequestDetailDAO;
    //private final AssetRequestDAO assetRequestDAO;
    //private final AssetsDAO assetsDAO;
    private final AssetService assetService;
    private final AssetRequestService assetRequestService;
    private final AllocationService allocationService;

    @Transactional
    public void execute(Long requestId) {
        // 1. Load detail
        //AssetInternalRequestDetail detail = assetInternalRequestDetailDAO.findByAssetRequestId(requestId);
        AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(requestId);
        if (detail == null) {
            throw new RuntimeException("Không tìm thấy chi tiết cấp phát cho Request ID: " + requestId);
        }

        // 2. Parse assigned asset IDs from note field (stored as "ASSIGNED_ASSETS:1,2,3")
        List<Long> assignedAssetIds = new ArrayList<>();
        if (detail.note != null && detail.note.startsWith("ASSIGNED_ASSETS:")) {
            String csv = detail.note.substring("ASSIGNED_ASSETS:".length());
            for (String part : csv.split(",")) {
                try {
                    assignedAssetIds.add(Long.parseLong(part.trim()));
                } catch (NumberFormatException ignored) {}
            }
        }

        if (assignedAssetIds.isEmpty()) {
            throw new RuntimeException("Không có tài sản nào được cấp phát trong yêu cầu này.");
        }

        // 3. Mark each assigned asset as ASSIGNED (status="02")
        List<Assets> toUpdate = new ArrayList<>();
        for (Long assetId : assignedAssetIds) {
            Assets asset = assetService.findById(assetId);
            if (asset != null) {
                asset.assetStatusId = AssetStatus.ASSIGNED.getValue();
                // Update locationId to the destination location
                if (detail.toLocationId != null) {
                    asset.locationId = detail.toLocationId;
                }
                toUpdate.add(asset);
            }
        }
        if (!toUpdate.isEmpty()) {
            allocationService.batchUpdateAllocation(toUpdate);
        }

        // 4. Determine completion: compare assigned count vs requested quantity
        int assignedCount = assignedAssetIds.size();
        int requestedQty = detail.quantity != null ? detail.quantity : 0;

        if (assignedCount >= requestedQty) {
            // Case 2: fully fulfilled → COMPLETED
//            AssetRequest request = assetRequestDAO.findAssetRequestByIdForUpdate(requestId).orElse(null);
            AssetRequest request = allocationService.getAssetRequestById(requestId).orElse(null);
            if (request != null) {
                request.requestStatusId = RequestStatus.COMPLETED.getValue();
//                assetRequestDAO.updateStatus(request);
                assetRequestService.updatePurchaseRequestStatus(request);
            }
        } else {
            // Case 1: partially fulfilled → mark is_done = true, keep IN_PROGRESS
            detail.isDone = true;
//            assetInternalRequestDetailDAO.updateIsDone(detail);
            allocationService.updateIsDone(detail);
        }
    }
}

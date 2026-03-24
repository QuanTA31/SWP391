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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessAllocationAssignmentUsecase {

    private final AssetService assetService;
    private final AssetRequestService assetRequestService;
    private final AllocationService allocationService;

    @Transactional
    public void execute(Long requestId, List<Long> selectedAssetIds) {
        // 1. Fetch Request
        AssetRequest request = allocationService.getAssetRequestById(requestId).orElse(null);
        if (request == null) {
            throw new RuntimeException("Request not found: " + requestId);
        }

        // 2. Load all detail records for this request (N records = N units)
        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
        if (details.isEmpty()) {
            throw new RuntimeException("Request detail not found for ID: " + requestId);
        }

        if (!selectedAssetIds.isEmpty()) {
            // Find unassigned detail records and pair them with selected asset IDs
            // Each detail row (asset_id == null) gets assigned one asset from the selection
            List<AssetInternalRequestDetail> unassigned = new ArrayList<>();
            for (AssetInternalRequestDetail d : details) {
                if (d.assetId == null) {
                    unassigned.add(d);
                }
            }

            List<Assets> toLock = new ArrayList<>();
            int pairCount = Math.min(selectedAssetIds.size(), unassigned.size());

            for (int i = 0; i < pairCount; i++) {
                Long assetId = selectedAssetIds.get(i);
                AssetInternalRequestDetail detail = unassigned.get(i);

                Assets asset = assetService.findById(assetId);
                if (asset != null) {
                    detail.setAssetId(assetId);
                    detail.setFromLocationId(asset.locationId);
                    detail.setFromUserId(asset.currentUserId);
                    // Reset is_done = null so Warehouse can dispatch this batch
                    detail.setIsDone(null);
                    allocationService.updateInternalDetail(detail);

                    // Mark asset as TRANSFERRING
                    if (AssetStatus.NEW.getValue().equals(asset.assetStatusId)
                         || AssetStatus.STOCK_IN.getValue().equals(asset.assetStatusId)
                         || AssetStatus.STOCKED.getValue().equals(asset.assetStatusId)
                         || AssetStatus.ASSIGNED.getValue().equals(asset.assetStatusId)) {
                        asset.assetStatusId = AssetStatus.TRANSFERRING.getValue(); // "03"
                        toLock.add(asset);
                    }
                }
            }

            if (!toLock.isEmpty()) {
                allocationService.batchUpdateAllocation(toLock);
            }
        }

        // 3. Set Status to IN_PROGRESS
        request.setRequestStatusId(RequestStatus.IN_PROGRESS.getValue());
        assetRequestService.updatePurchaseRequestStatus(request);
    }
}

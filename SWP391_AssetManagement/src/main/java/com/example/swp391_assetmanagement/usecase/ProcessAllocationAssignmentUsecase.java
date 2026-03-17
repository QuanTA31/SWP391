package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessAllocationAssignmentUsecase {

    private final AssetsDAO assetsDAO;
    private final AssetRequestDAO assetRequestDAO;
    private final AllocationService allocationService;

    @Transactional
    public void execute(Long requestId, List<Long> selectedAssetIds) {
        // 1. Fetch Request & Detail
        AssetRequest request = assetRequestDAO.findAssetRequestByIdForUpdate(requestId).orElse(null);
        if (request == null) {
            throw new RuntimeException("Request not found: " + requestId);
        }
        
        AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(requestId);
        if (detail == null) {
            throw new RuntimeException("Request detail not found for ID: " + requestId);
        }

        // 2. Map multiple assets to the single detail row
        // We Use the 'note' field to store the selected asset IDs as a comma-separated string
        // This allows the "View" screen to find and display them later.
        if (!selectedAssetIds.isEmpty()) {
            // Collect existing assigned asset IDs (if any) from the note field
            java.util.LinkedHashSet<Long> allAssetIds = new java.util.LinkedHashSet<>();

            String existingNote = detail.note;
            if (existingNote != null && existingNote.startsWith("ASSIGNED_ASSETS:")) {
                String csv = existingNote.substring("ASSIGNED_ASSETS:".length());
                for (String part : csv.split(",")) {
                    try {
                        allAssetIds.add(Long.parseLong(part.trim()));
                    } catch (NumberFormatException ignored) {}
                }
            }

            // Append new IDs (LinkedHashSet ensures no duplicates, preserves order)
            allAssetIds.addAll(selectedAssetIds);

            // Set the first one as the primary assetId for legacy compatibility
            detail.setAssetId(allAssetIds.iterator().next());

            // Write merged list back to note
            String mergedCsv = allAssetIds.stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.joining(","));

            detail.setNote("ASSIGNED_ASSETS:" + mergedCsv);
            allocationService.updateInternalDetail(detail);

            // Mark ALL newly assigned assets (and any legacy ones that failed to update before) as TRANSFERRING
            java.util.List<Assets> toLock = new java.util.ArrayList<>();
            for (Long aid : allAssetIds) {
                Assets asset = assetsDAO.findById(aid);
                // If it's already 02 (ASSIGNED), leave it alone.
                // If it's 00 (STOCKED) or 08 (RECOVERED), force it to 03 (TRANSFERRING).
                if (asset != null && (AssetStatus.STOCK_IN.getValue().equals(asset.assetStatusId) || AssetStatus.STOCKED.getValue().equals(asset.assetStatusId))) {
                    asset.assetStatusId = AssetStatus.TRANSFERRING.getValue(); // "03"
                    toLock.add(asset);
                }
            }
            if (!toLock.isEmpty()) {
                assetsDAO.batchUpdate(toLock);
            }

            // Reset is_done to null so Warehouse can dispatch the new batch of assets
            // (is_done may be true from a previous round confirmed by Department)
            if (Boolean.TRUE.equals(detail.isDone) || Boolean.FALSE.equals(detail.isDone)) {
                detail.setIsDone(null);
                allocationService.updateIsDone(detail);
            }
        }

        // 3. Set Status to IN_PROGRESS (05)
        request.setRequestStatusId(RequestStatus.IN_PROGRESS.getValue()); 
        assetRequestDAO.updateStatus(request);
    }
}

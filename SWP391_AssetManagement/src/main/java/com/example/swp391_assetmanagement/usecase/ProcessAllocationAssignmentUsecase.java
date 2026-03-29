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
        // 1. Lấy thông tin assetRequest từ DB, đảm bảo đơn hàng tồn tại
        AssetRequest request = allocationService.getAssetRequestById(requestId).orElse(null);
        if (request == null) {
            throw new RuntimeException("Request not found: " + requestId);
        }

        // 2. Lấy các dòng chi tiết
        List<AssetInternalRequestDetail> details = allocationService.getInternalDetailsByRequestId(requestId);
        if (details.isEmpty()) {
            throw new RuntimeException("Request detail not found for ID: " + requestId);
        }

        // 3. Ghép cặp(vòng lặp)
        if (!selectedAssetIds.isEmpty()) {
            // Tìm các bản ghi chi tiết chưa được gán và ghép chúng với ID tài sản đã chọn
            // Mỗi hàng chi tiết (asset_id == null) sẽ được gán một tài sản từ danh sách đã chọn
            List<AssetInternalRequestDetail> unassigned = new ArrayList<>();
            for (AssetInternalRequestDetail d : details) {
                if (d.assetId == null) {
                    unassigned.add(d);
                }
            }

            List<Assets> toLock = new ArrayList<>();
            // Xác định số lượng khớp
            int pairCount = Math.min(selectedAssetIds.size(), unassigned.size());

            // Lấy từng tài sản người dùng vừa chọn để điền vào các vị trí còn trống.
            for (int i = 0; i < pairCount; i++) {
                Long assetId = selectedAssetIds.get(i);
                AssetInternalRequestDetail detail = unassigned.get(i);

                Assets asset = assetService.findById(assetId);
                if (asset != null) {
                    detail.setAssetId(assetId);
                    detail.setFromLocationId(asset.locationId);
                    detail.setFromUserId(asset.currentUserId);
                    detail.setIsDone(null);

                    // Lưu thông tin dòng chi tiết
                    allocationService.updateInternalDetail(detail);

                    // Mark asset as TRANSFERRING
                    // Nếu trạng thái là NEW/STOCKED/ASSIGNED sẽ chuyển sang status
                    if (AssetStatus.NEW.getValue().equals(asset.assetStatusId)
                         || AssetStatus.STOCK_IN.getValue().equals(asset.assetStatusId)
                         || AssetStatus.STOCKED.getValue().equals(asset.assetStatusId)
                         || AssetStatus.ASSIGNED.getValue().equals(asset.assetStatusId)) {
                        asset.assetStatusId = AssetStatus.TRANSFERRING.getValue(); // "03"
                        toLock.add(asset);
                    }
                }
            }
            // cập nhật hàng loạt trạng thái các tài sản đã chọn thành "03"
            if (!toLock.isEmpty()) {
                allocationService.batchUpdateAllocation(toLock);
            }
        }

        // 3. Set Status to IN_PROGRESS
        request.setRequestStatusId(RequestStatus.IN_PROGRESS.getValue());
        assetRequestService.updatePurchaseRequestStatus(request);
    }
}

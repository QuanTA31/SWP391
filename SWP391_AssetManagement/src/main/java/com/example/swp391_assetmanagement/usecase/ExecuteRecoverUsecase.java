package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.CreateRequestRecoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExecuteRecoverUsecase {

    private final CreateRequestRecoverService recoverService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(List<String> assetCodes, String userCode) {
        // 1. Lấy ID Manager từ Code
        Long userId = recoverService.getUserIdByCode(userCode);

        // 2. SELECT danh sách tài sản dựa trên mã Code người dùng chọn từ UI
        List<Assets> selectedAssets = recoverService.getAssetsByCodes(assetCodes);

        // 3. Tạo Request tổng
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setRequestTypeId(RequestType.RETRIEVAL.getValue());
        assetRequest.setRequestedBy(userId);
        assetRequest.setRequestedDate(LocalDate.now());
        assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue());

        // Lưu Request và lấy ID sinh ra
        Long assetRequestId = assetRequestService.createPurchaseRequestForm(assetRequest);

        // 4. Lưu chi tiết và gom danh sách ID tài sản để Update trạng thái
        List<Long> assetIdsToUpdate = selectedAssets.stream()
                .map(asset -> {
                    // Tạo detail
                    AssetInternalRequestDetail detail = new AssetInternalRequestDetail();
                    detail.setAssetRequestId(assetRequestId);
                    detail.setAssetId(asset.id);
                    detail.setAssetTypeId(asset.assetTypeId);
                    detail.setQuantity(1);
                    detail.setFromLocationId(asset.locationId);
                    detail.setToLocationId("05");
                    detail.setFromUserId(asset.currentUserId);
                    detail.setIsDone(false);

                    // Lưu từng detail bằng hàm của bạn
                    recoverService.createRequestInternalRecover(detail);

                    return asset.id; // Trả về ID để gom vào list update
                })
                .collect(Collectors.toList());

        // 5. UPDATE trạng thái tất cả tài sản đã chọn sang '09' (RETRIEVAL)
        // Truyền list ID động, không hề hardcode
        recoverService.updateAssetsToRetrieval(assetIdsToUpdate);
    }
}
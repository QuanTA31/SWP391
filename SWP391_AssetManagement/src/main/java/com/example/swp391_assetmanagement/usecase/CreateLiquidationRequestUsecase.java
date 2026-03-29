package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.AssetsAssetRequestExternal;
import com.example.swp391_assetmanagement.enums.*;
import com.example.swp391_assetmanagement.service.*;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetLiquiServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreateLiquidationRequestUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetsAssetRequestExternalService assetsAssetRequestExternalService;
    private final AssetRequestService assetRequestService;
    private final AssetService assetService;
    private final UserService userService;

    @Transactional
    public void execute(List<Long> assetIds, HttpSession session) { //String note
        // no asset -> do nothing
        if (CollectionUtils.isEmpty(assetIds)) {
            return;
        }

        Long userId = userService.getIdByUserCode(session.getAttribute("USER_CODE").toString());

        // Check asset status (Not IN_PROGRESS or LOST)
        int invalidCount = assetService.checkAssetStatusInvalid(assetIds);
        if (invalidCount > 0) {
            throw new IllegalArgumentException("Selected assets contain invalid status (IN_PROGRESS or LOST)");
        }
        // ===== TAO REQUEST =====
        AssetRequest assetRequest = new AssetRequest();

        assetRequest.setRequestTypeId(RequestType.LIQUIDATION.getValue()); // request: thanh ly
        assetRequest.setRequestedBy(userId);
        assetRequest.setRequestedDate(LocalDate.now());
        assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue()); // manager create -> auto approve
        assetRequest.setApprovedBy(userId);
        assetRequest.setApprovedDate(LocalDate.now());
        //assetRequest.setNote(note);

        // Insert to AssetRequest
        Long assetRequestId = assetRequestService.createPurchaseRequestForm(assetRequest);

        // Get asset info
        List<AssetLiquiServiceResponse> assetLiquiServiceResponses = assetService.findByIdOfLiquidation(assetIds);

        // gom asset theo assetTypeId (quantity trong assetRequest)
        Map<String, List<AssetLiquiServiceResponse>> assetMap = new HashMap<>();
        for (AssetLiquiServiceResponse asset : assetLiquiServiceResponses) {
            assetMap.computeIfAbsent(asset.getAssetTypeId(), k -> new ArrayList<>()).add(asset);
        }

        // Insert DB
        for (Map.Entry<String, List<AssetLiquiServiceResponse>> entry : assetMap.entrySet()) {
            String assetTypeId = entry.getKey();
            List<AssetLiquiServiceResponse> assetsInGroup = entry.getValue();

            AssetExternalRequestDetail detail = new AssetExternalRequestDetail();

            detail.setAssetRequestId(assetRequestId);
            detail.setAssetTypeId(assetTypeId);
            detail.setQuantity(assetsInGroup.size());
            detail.setExternalStatusId(ExternalStatus.IN_PROGRESS.getValue());

            Long externalId = assetExternalRequestDetailService.insert(detail);

            // Insert to assets_asset_request_external
            List<AssetsAssetRequestExternal> assetRequestExternals = assetsInGroup.stream().map(e -> {
                AssetsAssetRequestExternal assetsAssetRequestExternal = new AssetsAssetRequestExternal();
                assetsAssetRequestExternal.setAssetId(e.assetId);
                assetsAssetRequestExternal.setAssetExternalRequestDetailId(externalId);
                assetsAssetRequestExternal.setCreatedAt(LocalDateTime.now());
                return assetsAssetRequestExternal;
            }).toList();
            assetsAssetRequestExternalService.batchInsert(assetRequestExternals);
        }
        // chuyen asset sang trang thai liquidation
        assetService.updateStatusByIds(assetIds, AssetStatus.LIQUIDATION);
    }
}
package com.example.swp391_assetmanagement.usecase;


import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.AssetSequences;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.entity.AssetsAssetRequestExternal;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.*;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetExternalRequestDetailServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseCreateAssetsUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetSequencesService assetSequencesService;
    private final AssetService assetService;
    private final AssetsAssetRequestExternalService assetsAssetRequestExternalService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(Long assetRequestId, HttpSession session) {

        List<AssetExternalRequestDetailServiceResponse> externalRequestDetailServiceResponses =
                assetExternalRequestDetailService.findByAssetRequestId(assetRequestId);

        if (CollectionUtils.isEmpty(externalRequestDetailServiceResponses)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            for (AssetExternalRequestDetailServiceResponse request : externalRequestDetailServiceResponses) {

                // Get asset_code
                AssetSequences assetSequences = assetSequencesService.findByIdToUpdate(
                        AssetType.hasValue(request.getAssetTypeId()) ? request.getAssetTypeId() : null);

                long newValue = assetSequences.currentValue + request.getQuantity();

                List<Assets> assetsList = new ArrayList<>();

                // Insert to assets
                for (Long i = assetSequences.currentValue; i < newValue; i++) {
                    Assets assets = new Assets();
                    assets.setAssetCode(String.format("%s-%04d", AssetType.of(request.getAssetTypeId()).getName(), i));
                    assets.setAssetStatusId(AssetStatus.STOCK_IN.getValue());
                    assets.setAssetTypeId(AssetType.of(request.getAssetTypeId()).getValue());
                    assets.setWarrantyPeriod(request.getWarrantyPeriod());
                    assets.setOriginalPrice(request.getUnitPrice());
                    assets.setDescription(request.getDescription());
                    assets.setLocationId(Location.WAREHOUSE.getValue());
                    assets.setReceivedDate(LocalDate.now());
                    assetsList.add(assets);
                }
                assetService.insertAsset(assetsList);

                // Update asset_code
                assetSequences.setCurrentValue(newValue);
                assetSequencesService.update(assetSequences);

                List<Assets> assets = assetService.findIdByStatus(AssetStatus.STOCK_IN.getValue());

                // Insert to assets_asset_request_external
                List<AssetsAssetRequestExternal> assetRequestExternals = assets.stream().map(e -> {
                    AssetsAssetRequestExternal assetsAssetRequestExternal = new AssetsAssetRequestExternal();
                    assetsAssetRequestExternal.setAssetId(e.id);
                    assetsAssetRequestExternal.setAssetExternalRequestDetailId(request.getId());
                    assetsAssetRequestExternal.setCreatedAt(LocalDateTime.now());
                    return assetsAssetRequestExternal;
                }).toList();
                assetsAssetRequestExternalService.batchInsert(assetRequestExternals);

                // update status assets
                assetService.updateAsset(assets);

                // Update asset_request
                AssetRequest assetRequest = new AssetRequest();
                assetRequest.setId(assetRequestId);
                assetRequest.setRequestStatusId(RequestStatus.STOCK_IN.getValue());
                assetRequestService.updatePurchaseRequestStatus(assetRequest);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}

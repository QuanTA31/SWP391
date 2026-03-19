package com.example.swp391_assetmanagement.usecase;


import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.AssetSequences;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.entity.AssetsAssetRequestExternal;
import com.example.swp391_assetmanagement.enums.*;
import com.example.swp391_assetmanagement.service.*;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetExternalRequestDetailServiceResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(assetRequestId);

        if (ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.PROCUREMENT.getValue())) {
            throw new ValidationException();
        }

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

                Integer startValue = assetSequences.currentValue;
                Integer quantity = request.getQuantity();
                Integer newValue = startValue + quantity;

                // Update asset_code

                assetSequences.setCurrentValue(newValue);
                assetSequencesService.update(assetSequences);

                List<Assets> assetsList = new ArrayList<>();

                // Insert to assets
                for (Integer i = startValue; i < newValue; i++) {
                    Assets assets = new Assets();
                    assets.setAssetCode(String.format("%s-%05d", AssetType.of(request.getAssetTypeId()).getName(), i));
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
                assetRequest.setId(request.getAssetRequestId());
                assetRequest.setRequestStatusId(RequestStatus.STOCK_IN.getValue());
                assetRequestService.updatePurchaseRequestStatus(assetRequest);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}

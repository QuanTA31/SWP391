package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetRequest;

import java.util.Optional;

public interface AssetRequestService {

    String findRequestTypeById(Long assetRequestId);

    Long createPurchaseRequestForm(AssetRequest assetRequest);

    Optional<AssetRequest> findAssetRequestByIdForUpdate(Long assetRequestId);

    void updatePurchaseRequest(AssetRequest assetRequest);

    void updateLiquidationRequest(AssetRequest assetRequest);

    void updatePurchaseRequestStatus(AssetRequest assetRequest);

    Integer countById(Long assetRequestId, String status);

    AssetRequest findByUpdate(Long assetRequestId);

    int updateIsSelected(AssetRequest assetRequest);

    Long findIdByAssetRequestDetailId(Long assetRequestDetailId);

    int moveInProgress(Long requestId);
}

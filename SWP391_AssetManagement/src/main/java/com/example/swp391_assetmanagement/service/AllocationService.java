package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;

public interface AllocationService {

    Long createAssetRequest(AssetRequest req);
    void updateAssetRequest(AssetRequest req);
    void createInternalDetail(AssetInternalRequestDetail detail);
    void updateInternalDetail(AssetInternalRequestDetail detail);
    void updateIsDone(AssetInternalRequestDetail detail);
    com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail getInternalDetailByRequestId(Long requestId);
    java.util.Optional<AssetRequest> getAssetRequestById(Long requestId);
}

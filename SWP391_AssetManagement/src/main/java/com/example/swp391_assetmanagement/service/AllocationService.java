package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;

import java.util.List;
import java.util.Optional;

public interface AllocationService {

    Long createAssetRequest(AssetRequest req);

    void updateAssetRequest(AssetRequest req);

    void createInternalDetail(AssetInternalRequestDetail detail);

    void batchCreateInternalDetails(List<AssetInternalRequestDetail> details);

    void deleteInternalDetailsByRequestId(Long requestId);

    void updateInternalDetail(AssetInternalRequestDetail detail);

    void updateIsDone(AssetInternalRequestDetail detail);

    AssetInternalRequestDetail getInternalDetailByRequestId(Long requestId);

    List<AssetInternalRequestDetail> getInternalDetailsByRequestId(Long requestId);

    Optional<AssetRequest> getAssetRequestById(Long requestId);

    int[] batchUpdateAllocation(List<Assets> assetsList);
}

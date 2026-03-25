package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;

import java.util.List;

public interface InventoryService {
    Long insertRequest(AssetRequest entity);
    int insertDetail(AssetInternalRequestDetail entity);
    List<InventoryItemServiceResponse> selectItems(Long requestId, String assetTypeId, String fullName);
    int countUnfinishedItems(Long requestId);
    int updateRequest(AssetRequest entity);
    int updateDetail(AssetInternalRequestDetail entity);
    int updateAssetStatus(Assets assets);
}

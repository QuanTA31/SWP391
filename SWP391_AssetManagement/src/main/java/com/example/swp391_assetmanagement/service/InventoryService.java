package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryProcessServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryActionServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetInternalRequestDetailServiceResponse;

import java.util.List;

public interface InventoryService {
    Long insertRequest(AssetRequest entity);
    int insertDetail(AssetInternalRequestDetail entity);
    List<InventoryItemServiceResponse> selectItems(InventoryProcessServiceRequest request);
    List<Assets> findByLocation(String locationId);
    int countUnfinishedItems(InventoryActionServiceRequest request);
    int updateRequest(AssetRequest entity);
    int updateDetail(AssetInternalRequestDetail entity);
    int updateAssetStatus(Assets assets);
    int updateAssetStatusAndNote(Assets assets);
    List<AssetInternalRequestDetailServiceResponse> selectAllDetails(InventoryActionServiceRequest request);
}

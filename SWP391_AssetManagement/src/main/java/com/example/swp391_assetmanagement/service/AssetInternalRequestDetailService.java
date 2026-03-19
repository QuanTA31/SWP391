package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;

import java.util.List;

public interface AssetInternalRequestDetailService {

    int insert(AssetInternalRequestDetail detail);

    List<AssetForRepairServiceResponse> findAssetsByLocationId(String locationId);

    AssetInternalRequestDetail findByAssetRequestId(Long assetRequestId);

    int update(AssetInternalRequestDetail detail);

    void createDetail(AssetInternalRequestDetail detail);

    void batchInsert(List<AssetInternalRequestDetail> details);
}

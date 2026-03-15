package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;

import java.util.List;

public interface AssetInternalRequestDetailService {

    int insert(AssetInternalRequestDetail detail);

    List<AssetForRepairServiceResponse> findAssetsByLocationId(String locationId);
}

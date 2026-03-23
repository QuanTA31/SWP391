package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;

import java.util.List;

public interface CreateRequestRecoverService {

    Long createRequestRecover(AssetRequest request);

    Long insertRecover(AssetRequest request);

    void createRequestInternalRecover(AssetInternalRequestDetail detail);

    List<Assets> getAssetsByCodes(List<String> assetCodes);

    Long getUserIdByCode(String userCode);

    void updateAssetsToRetrieval(List<Long> assetIds);
}

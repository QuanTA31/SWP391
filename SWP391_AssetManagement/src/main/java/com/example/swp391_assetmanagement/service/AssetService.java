package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.LiquiAssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetLiquiServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LiquiAssetViewAllServiceResponse;

import java.util.List;

public interface AssetService {

    List<AssetViewAllServiceResponse> viewAllAsset(AssetViewAllServiceRequest assetRequest);

    int[] insertAsset(List<Assets> assetsList);

    List<Assets> findIdByStatus(String status);

    int[] updateAsset(List<Assets> assetsList);

    List<LiquiAssetViewAllServiceResponse> liquiViewAllAsset(LiquiAssetViewAllServiceRequest assetRequest);

    List<AssetLiquiServiceResponse> findByIdOfLiquidation(List<Long> assetId);
}

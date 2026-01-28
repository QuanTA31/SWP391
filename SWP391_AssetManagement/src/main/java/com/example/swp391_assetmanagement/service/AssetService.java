package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllResponse;

import java.util.List;

public interface AssetService {

    List<AssetViewAllResponse> viewAllAsset(AssetViewAllRequest assetRequest);
}

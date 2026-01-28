package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;

import java.util.List;
import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetResponse;

public interface AssetService {

    List<AssetViewAllServiceResponse> viewAllAsset(AssetViewAllServiceRequest assetRequest);
    boolean authenticate(LoginRequest request);
    AssetResponse createAsset(AssetRequest assetRequest);
}

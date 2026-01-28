package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetResponse;

public interface AssetService {
    boolean authenticate(LoginRequest request);
    AssetResponse createAsset(AssetRequest assetRequest);
}

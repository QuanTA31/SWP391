package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetResponse;

public interface AssetService {

    AssetResponse createAsset(AssetRequest assetRequest);
}

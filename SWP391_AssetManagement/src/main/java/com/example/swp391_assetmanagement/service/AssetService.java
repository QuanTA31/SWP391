package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;

import java.util.List;

public interface AssetService {

    List<AssetViewAllServiceResponse> viewAllAsset(AssetViewAllServiceRequest assetRequest);
}

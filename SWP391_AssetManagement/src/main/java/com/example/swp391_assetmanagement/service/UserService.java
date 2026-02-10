package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginResponse;

public interface UserService {

    LoginResponse authenticate(LoginRequest request);

    LocationViewAssetResponse getLocationViewAsset(String userCode);
}

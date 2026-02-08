package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.UserLoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.UserLoginResponse;

public interface UserService {

    UserLoginResponse authenticate(UserLoginRequest request);

    LocationViewAssetResponse getLocationViewAsset(String userCode);
}

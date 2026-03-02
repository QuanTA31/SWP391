package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.LoginServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginServiceResponse;

public interface UserService {

    LoginServiceResponse authenticate(LoginServiceRequest request);

    LocationViewAssetServiceResponse getLocationViewAsset(String userCode);

    Long getIdByUserCode(String userCode);

    String getRoleCodesByUserCode(String userCode);
}

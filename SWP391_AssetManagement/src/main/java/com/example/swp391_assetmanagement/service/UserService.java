package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.LoginServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginServiceResponse;

import com.example.swp391_assetmanagement.service.serviceresponse.UserDropdownResponse;

import java.util.List;

public interface UserService {

    LoginServiceResponse authenticate(LoginServiceRequest request);

    LocationViewAssetServiceResponse getLocationViewAsset(String userCode);

    Long getIdByUserCode(String userCode);

    String getRoleIdByUserCode(String userCode);

    List<UserDropdownResponse> getActiveUsersByLocation(String locationId);

    String getUserNameById(Long id);
}

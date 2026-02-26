package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAllUserServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAllUserServiceResponse;

import java.util.List;

public interface ViewAllUserService {
    List<ViewAllUserServiceResponse> selectAllUser(ViewAllUserServiceRequest request);
}

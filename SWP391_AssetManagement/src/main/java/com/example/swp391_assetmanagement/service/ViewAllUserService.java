package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAllUserRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAllUserResponse;

import java.util.List;

public interface ViewAllUserService {
    List<ViewAllUserResponse> selectAllUser(ViewAllUserRequest request);
}

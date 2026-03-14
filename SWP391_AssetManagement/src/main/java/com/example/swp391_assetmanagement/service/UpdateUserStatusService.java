package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.service.servicerequest.UpdateUserStatusRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.UpdateUserStatusResponse;

public interface UpdateUserStatusService {
    int UpdateStatus(Users users);
}

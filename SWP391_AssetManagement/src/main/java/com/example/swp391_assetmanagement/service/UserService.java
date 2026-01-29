package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.dto.request.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.UserDAOResponse;

public interface UserService {
    UserDAOResponse authenticate(LoginRequest request);
}

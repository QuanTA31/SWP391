package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.dto.request.LoginRequest;

public interface UserService {
    boolean authenticate(LoginRequest request);
}

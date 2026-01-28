package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.LoginRequest;
import com.example.swp391_assetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUsecase {

    private final UserService userService; // Gọi Service

    public boolean executeLogin(LoginRequest request) {
        return userService.authenticate(request);
    }
}

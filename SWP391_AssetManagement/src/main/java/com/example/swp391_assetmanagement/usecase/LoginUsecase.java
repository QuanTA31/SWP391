package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.LoginDTORequest;
import com.example.swp391_assetmanagement.dto.response.LoginDTOResponse;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUsecase {

    private UserService userService; // Gọi Service

    @Transactional(readOnly = true)
    public LoginDTOResponse executeLogin(LoginDTORequest request) {

        LoginResponse loginResponse =  userService.authenticate(LoginRequest.builder()
                        .username(request.getUsername())
                        .password(request.getPassword())
                .build());

        return LoginDTOResponse.builder()
                .userId(loginResponse.getId())
                .userName(loginResponse.getName())
                .roleId(loginResponse.getRoleId())
                .userCode(loginResponse.getUserCode())
                .build();
    }
}

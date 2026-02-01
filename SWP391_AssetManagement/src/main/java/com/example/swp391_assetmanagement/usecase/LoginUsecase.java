package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.LoginDTORequest;
import com.example.swp391_assetmanagement.dto.response.UserLoginDTOResponse;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.UserLoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUsecase {

    private final UserService userService; // Gọi Service

    @Transactional(readOnly = true)
    public UserLoginDTOResponse executeLogin(LoginDTORequest request) {

        UserLoginResponse userLoginResponse =  userService.authenticate(UserLoginRequest.builder()
                        .username(request.getUsername())
                        .password(request.getPassword())
                .build());

        return UserLoginDTOResponse.builder()
                .name(userLoginResponse.getName())
                .roleId(userLoginResponse.getRoleId())
                .build();
    }
}

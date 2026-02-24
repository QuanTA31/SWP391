package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.LoginDTORequest;
import com.example.swp391_assetmanagement.dto.response.LoginDTOResponse;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.LoginServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUsecase {

    private final UserService userService; // Gọi Service

    @Transactional(readOnly = true)
    public LoginDTOResponse executeLogin(LoginDTORequest request) {

        LoginServiceResponse loginResponse =  userService.authenticate(LoginServiceRequest.builder()
                        .username(request.getUsername())
                        .password(request.getPassword())
                .build());

        if(loginResponse != null) {
            return LoginDTOResponse.builder()
                    .userId(loginResponse.getId())
                    .userName(loginResponse.getUsername())
                    .roleId(loginResponse.getRoleId())
                    .userCode(loginResponse.getUserCode())
                    .build();
        }else {
            return null;
        }
    }
}

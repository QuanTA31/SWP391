package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginServiceRequest {

    private String username;

    private String password;
}

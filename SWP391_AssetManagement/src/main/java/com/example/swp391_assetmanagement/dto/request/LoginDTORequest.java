package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDTORequest {

    public final String username;

    public final String password;
}
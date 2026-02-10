package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginDTOResponse {

    private final Long userId;

    private final String userName;

    private final String userCode;

    private final String roleId;
}

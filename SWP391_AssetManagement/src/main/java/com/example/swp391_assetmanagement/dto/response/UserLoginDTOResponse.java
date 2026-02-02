package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginDTOResponse {

    public String userName;

    public String userCode;

    public String roleId;
}

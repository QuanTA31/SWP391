package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.seasar.doma.Column;

import java.time.LocalDate;

@Getter
@Builder
public class UserDTOResponse {

    private final String userCode;

    private final String username;

    private final String password;

    private final String roleName;

    private final String userStatus;

    private final String locationName;

    private final String name;

    private final String phone;

    private final String email;

    private final LocalDate createAt;
}

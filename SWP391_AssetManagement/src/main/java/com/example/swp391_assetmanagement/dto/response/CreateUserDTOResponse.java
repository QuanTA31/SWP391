package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class CreateUserDTOResponse {

    private String userCode;

    private String username;

    private String password;

    private String roleId;

    private String statusId;

    private String name;

    private String phone;

    private String email;

    private LocalDate dateOfBirth;

    private String locationId;
}

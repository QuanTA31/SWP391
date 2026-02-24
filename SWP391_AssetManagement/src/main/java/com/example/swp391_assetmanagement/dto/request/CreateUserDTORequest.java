package com.example.swp391_assetmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CreateUserDTORequest {

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
package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Getter
@Builder

public class ChangePasswordDTORequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}

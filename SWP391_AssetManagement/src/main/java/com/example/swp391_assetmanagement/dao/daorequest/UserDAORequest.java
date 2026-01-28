package com.example.swp391_assetmanagement.dao.daorequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDAORequest {
    private String username;
    private String password;
}

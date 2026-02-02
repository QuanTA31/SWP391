package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Getter
@Entity
public class UserLoginResponse {

    @Column(name = "name")
    public String name;

    @Column(name = "role_id")
    public String roleId;

    @Column(name = "user_code")
    public String userCode;
}

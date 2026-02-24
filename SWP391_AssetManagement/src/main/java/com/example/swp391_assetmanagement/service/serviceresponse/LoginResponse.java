package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Getter
@Entity
public class LoginResponse {

    @Column(name = "id")
    public Long id;

    @Column(name = "username")
    public String username;

    @Column(name = "name")
    public String name;

    @Column(name = "role_id")
    public String roleId;

    @Column(name = "user_code")
    public String userCode;
}

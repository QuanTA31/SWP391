package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
public class ViewAllUserResponse {

    @Column(name = "user_code")
    public String userCode;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "role_id")
    public String roleId;

    @Column(name = "user_status")
    public String userStatus;

    @Column(name = "user_location_id")
    public String locationId;

    @Column(name = "name")
    public String name;

    @Column(name = "phone")
    public String phone;

    @Column(name = "email")
    public String email;
}

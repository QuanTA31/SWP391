package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Entity
@Getter
@Setter
public class UserDropdownResponse {

    @Column(name = "id")
    public Long id;

    @Column(name = "user_code")
    public String userCode;

    @Column(name = "name")
    public String name;
}

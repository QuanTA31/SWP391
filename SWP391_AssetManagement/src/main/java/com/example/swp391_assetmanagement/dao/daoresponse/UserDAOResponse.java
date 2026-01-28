package com.example.swp391_assetmanagement.dao.daoresponse;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;

@Entity
public class UserDAOResponse {
    @Column(name = "name")
    public String name;
    @Column(name = "role_id")
    public String roleId;
}

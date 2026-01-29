package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.dto.request.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.UserDAOResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface UserDAO {
    @Select
    UserDAOResponse findByUsername(LoginRequest userDAORequest);
}

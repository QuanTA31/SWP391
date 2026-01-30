package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.UserLoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.UserLoginResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface UserDAO {

    @Select
    UserLoginResponse findByUsername(UserLoginRequest userDAORequest);
}

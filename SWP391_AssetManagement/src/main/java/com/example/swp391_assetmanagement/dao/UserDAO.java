package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface UserDAO {

    @Select
    LoginResponse findByUsername(LoginRequest userDAORequest);

    @Select
    LocationViewAssetResponse findLocationByAssetCode(String asserCode);
}

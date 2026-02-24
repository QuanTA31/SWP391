package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.LoginRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@Dao
@ConfigAutowireable
public interface UserDAO {

    @Select
    Optional<LoginResponse> findByUsername(LoginRequest userDAORequest);

    @Select
    LocationViewAssetResponse findLocationByAssetCode(String asserCode);
}

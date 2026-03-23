package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.LoginServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LoginServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

import com.example.swp391_assetmanagement.service.serviceresponse.UserDropdownResponse;

@Dao
@ConfigAutowireable
public interface UserDAO {

    @Select
    Optional<LoginServiceResponse> findByUsername(LoginServiceRequest userDAORequest);

    @Select
    LocationViewAssetServiceResponse findLocationByAssetCode(String asserCode);

    @Select
    Long findIdByUserCode(String userCode);

    @Select
    String findRoleCodesByUserCode(String userCode);

    @Select
    String findUserNameById(Long id);

    @Select
    List<UserDropdownResponse> findUsersByLocationId(String locationId);
}

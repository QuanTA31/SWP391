package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAllUserServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAllUserServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AllUserDAO {
    @Select
    List<ViewAllUserServiceResponse> selectAllUser(ViewAllUserServiceRequest request);
}

package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.ViewAllUserRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAllUserResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AllUserDAO {
//    @Select
//    List<ViewAllUserResponse> selectAllUser(ViewAllUserRequest request);
}

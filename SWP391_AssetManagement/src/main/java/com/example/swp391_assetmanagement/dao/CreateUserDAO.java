package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.UserDetail;
import com.example.swp391_assetmanagement.entity.Users;
import com.example.swp391_assetmanagement.service.servicerequest.CreateUserServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.CreateUserServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@Dao
@ConfigAutowireable
public interface CreateUserDAO {

    @Select
    long CountTotalUsers();

    @Insert
    int insertUser(Users user); // Tự động điền user.id sau khi chạy

    @Insert
    int insertUserDetail(UserDetail detail);

    @Select
    boolean existsByUsername(String username);

    @Select
    boolean existsByEmail(String email);

    @Select
    boolean existsByPhone(String phone);
}

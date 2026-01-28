package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.dao.daorequest.UserDAORequest;
import com.example.swp391_assetmanagement.dao.daoresponse.UserDAOResponse;
import com.example.swp391_assetmanagement.entity.Users;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;
@Dao
@ConfigAutowireable
public interface UserDAO {
    @Select
    UserDAOResponse findByUsername(UserDAORequest userDAORequest);
}

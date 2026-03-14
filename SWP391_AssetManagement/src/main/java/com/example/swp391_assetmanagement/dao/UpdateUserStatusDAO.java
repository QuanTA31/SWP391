package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.Users;
import org.seasar.doma.Dao;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface UpdateUserStatusDAO {
    @Update(sqlFile = true)
    int changeStatusByUsername(Users user);
}

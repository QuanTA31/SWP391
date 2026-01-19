package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.dao.daoresponse.AssetsDaoResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface AssetsDAO {

    @Select
    AssetsDaoResponse selectById(Long id);
}

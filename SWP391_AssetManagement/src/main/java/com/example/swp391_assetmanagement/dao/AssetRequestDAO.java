package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface AssetRequestDAO {

    @Insert(sqlFile = true)
    int insert(AssetRequest assetRequest);
}

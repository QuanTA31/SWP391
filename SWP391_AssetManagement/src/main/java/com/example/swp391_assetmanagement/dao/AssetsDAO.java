package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import com.example.swp391_assetmanagement.service.specialrepository.AssetSpecialEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface AssetsDAO {

    @Select
    AssetSpecialEntity selectById(AssetRequest request);
}

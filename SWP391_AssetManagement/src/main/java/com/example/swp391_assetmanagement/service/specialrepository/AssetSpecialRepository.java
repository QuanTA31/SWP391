package com.example.swp391_assetmanagement.service.specialrepository;

import com.example.swp391_assetmanagement.service.servicerequest.AssetRequest;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

@Dao
@ConfigAutowireable
public interface AssetSpecialRepository {

    @Select
    AssetSpecialEntity selectById(AssetRequest request);
}

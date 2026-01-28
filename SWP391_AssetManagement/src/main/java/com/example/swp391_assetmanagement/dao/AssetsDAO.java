package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetsDAO {

    @Select
    List<AssetViewAllResponse> selectAssetAll(AssetViewAllRequest request);

//    @Update(sqlFile = true)
//    int updateById(AssetsDaoResponse assetsDaoResponse);
}

package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetsDAO {

    @Select
    List<AssetViewAllServiceResponse> selectAssetAll(AssetViewAllServiceRequest request);

//    @Update(sqlFile = true)
//    int updateById(AssetsDaoResponse assetsDaoResponse);
}

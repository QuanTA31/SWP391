package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.dao.daorequest.ViewAssetDAORequest;
import com.example.swp391_assetmanagement.dao.daoresponse.ViewAssetDAOResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetsDAO {

    @Select
    List<ViewAssetDAOResponse> selectAssetAll(ViewAssetDAORequest request);

//    @Update(sqlFile = true)
//    int updateById(AssetsDaoResponse assetsDaoResponse);
}

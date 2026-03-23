package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface CreateRequestRecoverDAO {
    @Insert(sqlFile = true)
    int insertRequestRecover(AssetRequest request);

    @Insert(sqlFile = true)
    int insertRecover(AssetRequest assetRequest);

    @Insert(sqlFile = true)
    int inrsertAssetRecoverToSigleRequest(AssetInternalRequestDetail request);

    @Select
    List<Assets> selectAssetsByCodes(List<String> assetCodes);

    @Update(sqlFile = true)
    int updateAssetStatusToRetrival(List<Long> assetIds);
}

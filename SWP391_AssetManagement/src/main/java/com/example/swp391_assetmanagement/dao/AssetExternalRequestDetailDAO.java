package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetExternalRequestDetailDAO {

    @BatchInsert(sqlFile = true)
    int[] batchInsert(List<AssetExternalRequestDetail> details);

    @Select
    List<AssetExternalRequestDetail> selectByAssetRequestId(Long assetRequestId);

    @BatchUpdate(sqlFile = true)
    int[] batchUpdate(List<AssetExternalRequestDetail> details);

    @Select
    List<AssetExternalRequestDetail> selectByAssetRequestIdForUpdate(Long assetRequestId);
}

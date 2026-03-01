package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetsAssetRequestExternal;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetsAssetRequestExternalDAO {

    @BatchInsert(sqlFile = true)
    int[] batchInsert(List<AssetsAssetRequestExternal> externals);

}

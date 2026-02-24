package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetExternalRequestDetailDAO {

    @BatchInsert(sqlFile = true)
    int[] insert(List<AssetExternalRequestDetail> details);
}

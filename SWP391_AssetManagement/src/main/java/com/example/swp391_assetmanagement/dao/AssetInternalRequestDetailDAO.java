package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetInternalRequestDetailDAO {

    @Insert(sqlFile = true)
    int insert(AssetInternalRequestDetail detail);

    @Update(sqlFile = true)
    int update(AssetInternalRequestDetail detail);

    @Update(sqlFile = true)
    int updateIsDone(AssetInternalRequestDetail detail);

    @Select
    AssetInternalRequestDetail findByAssetRequestId(Long assetRequestId);

    @Select
    AssetInternalRequestDetail selectById(Long id);

    @Select
    List<AssetInternalRequestDetail> selectByRequestId(Long requestId);

    @Select
    int countRemainingItems(Long requestId);
}

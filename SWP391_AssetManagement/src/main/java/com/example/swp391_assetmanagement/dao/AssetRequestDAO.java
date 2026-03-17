package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.Optional;

@Dao
@ConfigAutowireable
public interface AssetRequestDAO {

    @Select
    String findRequestTypeById(Long assetRequestId);

    @Select
    java.util.Optional<AssetRequest> findAssetRequestByIdForUpdate(Long assetRequestId);

    @Insert(sqlFile = true)
    int insert(AssetRequest assetRequest);

    @Select
    Long getLastId();

    @Update(sqlFile = true)
    int update(AssetRequest assetRequest);

    @Update(sqlFile = true)
    int updateStatus(AssetRequest assetRequest);

    @Select
    Integer countById(Long assetRequestId, String status);

    @Select
    AssetRequest selectByUpdate(Long assetRequestId);

    @Update(sqlFile = true)
    int updateIsSelected(AssetRequest assetRequest);

    @Select
    Long findIdByAssetRequestDetailId(Long assetRequestDetailId);

    @Update(sqlFile = true)
    int moveInProgress(Long id, String researchDone, String inProgress);
}

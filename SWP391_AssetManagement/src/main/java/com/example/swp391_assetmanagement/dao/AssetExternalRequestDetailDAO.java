package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetExternalRequestDetailServiceResponse;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

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

    @Delete(sqlFile = true)
    int batchDelete(List<Long> details);

    @Select
    AssetExternalRequestDetail findById(Long id);

    @Select
    Long findAssetRequestId(Long assetRequestDetailId);

    @Select
    Integer countOptionDetail(Long assetRequestId);

    @Update(sqlFile = true)
    int updateExternalStatusId(Long id, String externalStatusId);

    @Select
    Integer countNotApprovedByRequestId(Long requestId);

    @Select
    List<AssetExternalRequestDetailServiceResponse> findByAssetRequestId(Long assetRequestId);
}

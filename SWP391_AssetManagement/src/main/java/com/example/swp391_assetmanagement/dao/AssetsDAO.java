package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.entity.AssetsAssetRequestExternal;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.BatchUpdate;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface AssetsDAO {

    @Select
    List<AssetViewAllServiceResponse> selectAssetAll(AssetViewAllServiceRequest request);


    @BatchInsert(sqlFile = true)
    int[] batchInsert(List<Assets> assetsList);

    @Select
    List<Assets> findByStatus(String status);

    @Select
    List<Assets> findByTypeAndStatus(String typeId, String statusId);

    @Select
    Assets findById(Long id);

    @BatchUpdate
    int[] batchUpdate(List<Assets> assetsList);
}

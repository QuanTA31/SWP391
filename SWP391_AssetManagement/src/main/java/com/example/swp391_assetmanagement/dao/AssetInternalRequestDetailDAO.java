package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;
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
    int insertOfMaintain(AssetInternalRequestDetail entity);

    @Select
    List<AssetForRepairServiceResponse> findByLocationId(String locationId);

    @Select
    AssetInternalRequestDetail findByAssetRequestIdOfMaintain(Long assetRequestId);

    @Update(sqlFile = true)
    int updateOfMaintain(AssetInternalRequestDetail entity);

    @Insert(sqlFile = true)
    int insert(AssetInternalRequestDetail detail);

    @Update(sqlFile = true)
    int update(AssetInternalRequestDetail detail);

    @Update(sqlFile = true)
    int updateIsDone(AssetInternalRequestDetail detail);

    @Select
    AssetInternalRequestDetail findByAssetRequestId(Long assetRequestId);
    //select request detail to update request detail
    @Select
    AssetInternalRequestDetail selectById(Long id);
    //find all request detail by request id
    @Select
    List<AssetInternalRequestDetail> selectByRequestId(Long requestId);
    //count request detail by request id
    @Select
    int countRemainingItems(Long requestId);
}

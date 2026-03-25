package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.entity.Location;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

@Dao
@ConfigAutowireable
public interface InventoryDAO {

    @Insert(sqlFile = true)
    int insertInventoryRequest(AssetRequest entity);

    @Insert(sqlFile = true)
    int insertInventoryDetail(AssetInternalRequestDetail entity);

    @Select
    List<InventoryItemServiceResponse> selectInventoryItems(Long requestId, String assetTypeId, String fullName);

    @Select
    int countUnfinishedInventoryItems(Long requestId);

    @Update(sqlFile = true)
    int updateInventoryRequest(AssetRequest entity);

    @Update(sqlFile = true)
    int updateInventoryDetail(AssetInternalRequestDetail entity);

    @Update(sqlFile = true)
    int updateAssetStatus(Assets assets);

    @Select
    List<Assets> selectAssetsByLocationAndStatus(String locationId, String status);

    @Select
    AssetInternalRequestDetail selectDetailById(Long id);

    @Select
    Assets selectAssetById(Long id);

    @Select
    List<Location> selectLocationsWithAssets();
}
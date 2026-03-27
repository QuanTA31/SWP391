package com.example.swp391_assetmanagement.dao;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.entity.Location;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryProcessServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryActionServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetInternalRequestDetailServiceResponse;
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
    List<InventoryItemServiceResponse> selectInventoryItems(InventoryProcessServiceRequest request, org.seasar.doma.jdbc.SelectOptions options);

    @Select
    int countInventoryItems(InventoryProcessServiceRequest request);

    @Select
    int countUnfinishedInventoryItems(InventoryActionServiceRequest request);

    @Update(sqlFile = true)
    int updateInventoryRequest(AssetRequest entity);

    @Update(sqlFile = true)
    int updateInventoryDetail(AssetInternalRequestDetail entity);

    @Update(sqlFile = true)
    int updateAssetStatus(Assets assets);

    @Update(sqlFile = true)
    int updateAssetStatusAndNote(Assets assets);

    @Select
    List<AssetInternalRequestDetailServiceResponse> selectAllDetailsByRequestId(InventoryActionServiceRequest request);

    @Select
    List<Assets> selectAssetsByLocationAndStatus(String locationId, String status);

    @Select
    List<Assets> selectAssetsByLocation(String locationId);

    @Select
    AssetInternalRequestDetail selectDetailById(Long id);

    @Select
    Assets selectAssetById(Long id);

    @Select
    List<Location> selectLocationsWithAssets();
}
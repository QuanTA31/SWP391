package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.InventoryDAO;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetLifecycleServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.LiquiAssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetLifecycleServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetLiquiServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import com.example.swp391_assetmanagement.service.serviceresponse.LiquiAssetViewAllServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetsDAO assetsDAO;
    private final InventoryDAO inventoryDAO;
    @Override
    public List<AssetViewAllServiceResponse> viewAllAsset(AssetViewAllServiceRequest assetRequest) {

        List<AssetViewAllServiceResponse> daoResponses = assetsDAO.selectAssetAll(assetRequest);

        if (daoResponses.isEmpty()) {
            return Collections.emptyList();
        }

        return daoResponses;
    }

    @Override
    public int[] insertAsset(List<Assets> assetsList) {
        return assetsDAO.batchInsert(assetsList);
    }

    @Override
    public List<Assets> findIdByStatus(String status) {
        return assetsDAO.findByStatus(status);
    }

    @Override
    public List<Assets> findByTypeAndStatus(String typeId, String statusId) {
        return assetsDAO.findByTypeAndStatus(typeId, statusId);
    }

    @Override
    public List<Assets> findStockByType(String typeId) {
        return assetsDAO.findStockByType(typeId);
    }

    @Override
    public List<Assets> findRecoveredByType(String typeId) {
        return assetsDAO.findRecoveredByType(typeId);
    }

    @Override
    public int[] updateAsset(List<Assets> assetsList) {
        return assetsDAO.batchUpdate(assetsList);
    }

    @Override
    public List<LiquiAssetViewAllServiceResponse> liquiViewAllAsset(LiquiAssetViewAllServiceRequest assetRequest) {

        List<LiquiAssetViewAllServiceResponse> daoResponses = assetsDAO.selectLiquiAssetAll(assetRequest);

        if (daoResponses.isEmpty()) {
            return Collections.emptyList();
        }

        return daoResponses;
    }

    @Override
    public List<AssetLiquiServiceResponse> findByIdOfLiquidation(List<Long> assetId) {
        return assetsDAO.findByIdOfLiquidation(assetId);
    }
    @Override
    public Assets findById(Long id) {
        return assetsDAO.findById(id);
    }

    @Override
    public void updateStatusByIds(List<Long> assetIds, AssetStatus status) {
        if (assetIds == null || assetIds.isEmpty()) return;

        assetsDAO.updateStatusByIds(assetIds, status.getValue());
    }

    @Override
    public List<Assets> findByLocationAndStatus(String locationId, String status) {
        return inventoryDAO.selectAssetsByLocationAndStatus(locationId, status);
    }

    @Override
    public Assets findByIdInventory(Long id) {
        return inventoryDAO.selectAssetById(id);
    }

    @Override
    public List<AssetLifecycleServiceResponse> viewAssetLifecycle(AssetLifecycleServiceRequest request) {
        List<AssetLifecycleServiceResponse> daoResponses = assetsDAO.selectAssetLifecycle(request);
        if (daoResponses.isEmpty()) {
            return Collections.emptyList();
        }
        return daoResponses;
    }

    @Override
    public Assets findByAssetCode(String assetCode) {
        return assetsDAO.selectByAssetCode(assetCode);
    }

    @Override
    public int checkAssetStatusForPurchase(List<Long> assetIds) {
        if (assetIds == null || assetIds.isEmpty()) {
            return 0;
        }
        List<String> invalidStatuses = List.of(AssetStatus.LOST.getValue(), AssetStatus.IN_PROGRESS.getValue());
        return assetsDAO.countInvalidAssetsInList(assetIds, invalidStatuses);
    }
}

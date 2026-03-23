package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.LiquiAssetViewAllServiceRequest;
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

}

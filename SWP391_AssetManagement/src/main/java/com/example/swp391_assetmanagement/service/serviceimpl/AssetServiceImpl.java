package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.daorequest.ViewAssetDAORequest;
import com.example.swp391_assetmanagement.dao.daoresponse.ViewAssetDAOResponse;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
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

        List<ViewAssetDAOResponse> daoResponses = assetsDAO.selectAssetAll(ViewAssetDAORequest.builder()
                .locationId(assetRequest.getLocationId())
                .assetTypeId(assetRequest.getAssetTypeId())
                .assetStatusId(assetRequest.getAssetStatusId())
                .offset((assetRequest.getPageIndex()-1)*assetRequest.getPageSize())
                .pageSize(assetRequest.getPageSize())
                .build());

        if (daoResponses.isEmpty()) {
            return Collections.emptyList();
        }

        return daoResponses.stream().map(
                        entity -> AssetViewAllServiceResponse.builder()
                                .assetCode(entity.assetCode)
                                .description(entity.description)
                                .originalPrice(entity.originalPrice)
                                .warrantyPeriod(entity.warrantyPeriod)
                                .receivedDate(entity.receivedDate)
                                .locationId(entity.locationId)
                                .assetStatusId(entity.assetStatusId)
                                .currentUserId(entity.currentUserId)
                                .assetTypeId(entity.assetTypeId)
                                .totalItems(entity.totalItems)
                                .build())
                .toList();
    }
}

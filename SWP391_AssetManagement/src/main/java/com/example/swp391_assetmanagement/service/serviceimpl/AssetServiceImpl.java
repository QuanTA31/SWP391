package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllResponse;
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
    public List<AssetViewAllResponse> viewAllAsset(AssetViewAllRequest assetRequest) {

        List<AssetViewAllResponse> daoResponses = assetsDAO.selectAssetAll(assetRequest);

        if (daoResponses.isEmpty()) {
            return Collections.emptyList();
        }

        return daoResponses;
    }
}

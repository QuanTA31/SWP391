package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetsAssetRequestExternalDAO;
import com.example.swp391_assetmanagement.entity.AssetsAssetRequestExternal;
import com.example.swp391_assetmanagement.service.AssetsAssetRequestExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetsAssetRequestExternalServiceImpl implements AssetsAssetRequestExternalService {

    private final AssetsAssetRequestExternalDAO assetsAssetRequestExternalDAO;

    @Override
    public int[] batchInsert(List<AssetsAssetRequestExternal> assetsAssetRequestExternals) {
        return assetsAssetRequestExternalDAO.batchInsert(assetsAssetRequestExternals);
    }
}

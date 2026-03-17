package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.CreateRequestRecoverDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.service.AssetInternalRequestDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetInternalRequestDetailServiceImpl implements AssetInternalRequestDetailService {

    private final CreateRequestRecoverDAO createRequestRecoverDAO;

    @Override
    public void createDetail(AssetInternalRequestDetail detail) {
        createRequestRecoverDAO.inrsertAssetRecoverToSigleRequest(detail);
    }

    @Override
    public void batchInsert(List<AssetInternalRequestDetail> details) {
        if (details == null || details.isEmpty()) return;
        for (AssetInternalRequestDetail detail : details) {
            createRequestRecoverDAO.inrsertAssetRecoverToSigleRequest(detail);
        }
    }
}
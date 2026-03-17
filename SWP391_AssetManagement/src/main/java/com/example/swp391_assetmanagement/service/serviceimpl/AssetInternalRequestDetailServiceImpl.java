package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetInternalRequestDetailDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.service.AssetInternalRequestDetailService;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetInternalRequestDetailServiceImpl implements AssetInternalRequestDetailService {

    private final AssetInternalRequestDetailDAO assetInternalRequestDetailDAO;

    @Override
    public int insert(AssetInternalRequestDetail detail) {
        return assetInternalRequestDetailDAO.insertOfMaintain(detail);
    }

    @Override
    public List<AssetForRepairServiceResponse> findAssetsByLocationId(String locationId) {
        return assetInternalRequestDetailDAO.findByLocationId(locationId);
    }

    @Override
    public AssetInternalRequestDetail findByAssetRequestId(Long assetRequestId) {
        return assetInternalRequestDetailDAO.findByAssetRequestIdOfMaintain(assetRequestId);
    }

    @Override
    public int update(AssetInternalRequestDetail detail) {
        return assetInternalRequestDetailDAO.updateOfMaintain(detail);
    }
}

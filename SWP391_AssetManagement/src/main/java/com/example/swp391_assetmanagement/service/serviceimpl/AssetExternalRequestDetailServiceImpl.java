package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetExternalRequestDetailDAO;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetExternalRequestDetailServiceImpl implements AssetExternalRequestDetailService {

    private final AssetExternalRequestDetailDAO  assetExternalRequestDetailDAO;

    @Override
    public int[] batchInsert(List<AssetExternalRequestDetail> details) {
        return assetExternalRequestDetailDAO.batchInsert(details);
    }

    @Override
    public List<AssetExternalRequestDetail> getByAssetRequestId(Long assetRequestId) {
        return assetExternalRequestDetailDAO.selectByAssetRequestId(assetRequestId);
    }

    @Override
    public int[] batchUpdate(List<AssetExternalRequestDetail> details) {
        return assetExternalRequestDetailDAO.batchUpdate(details);
    }

    @Override
    public List<AssetExternalRequestDetail> getByAssetRequestIdForUpdate(Long assetRequestId) {
        return assetExternalRequestDetailDAO.selectByAssetRequestIdForUpdate(assetRequestId);
    }

    @Override
    public void batchDelete(List<Long> idsToDelete) {
        assetExternalRequestDetailDAO.batchDelete(idsToDelete);
    }

    @Override
    public AssetExternalRequestDetail findToUpdate(Long id) {
        return assetExternalRequestDetailDAO.findById(id);
    }
}

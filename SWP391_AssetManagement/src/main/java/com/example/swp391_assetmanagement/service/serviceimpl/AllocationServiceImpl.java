package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetInternalRequestDetailDAO;
import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.AllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

    private final AssetRequestDAO assetRequestDAO;
    private final AssetInternalRequestDetailDAO assetInternalRequestDetailDAO;

    @Override
    public Long createAssetRequest(AssetRequest req) {
        assetRequestDAO.insert(req);
        return assetRequestDAO.getLastId();
    }

    @Override
    public void updateAssetRequest(AssetRequest req) {
        assetRequestDAO.update(req);
    }

    @Override
    public void createInternalDetail(AssetInternalRequestDetail detail) {
        assetInternalRequestDetailDAO.insert(detail);
    }

    @Override
    public void updateInternalDetail(AssetInternalRequestDetail detail) {
        assetInternalRequestDetailDAO.update(detail);
    }

    @Override
    public void updateIsDone(AssetInternalRequestDetail detail) {
        assetInternalRequestDetailDAO.updateIsDone(detail);
    }

    @Override
    public AssetInternalRequestDetail getInternalDetailByRequestId(Long requestId) {
        return assetInternalRequestDetailDAO.findByAssetRequestId(requestId);
    }

    @Override
    public java.util.Optional<AssetRequest> getAssetRequestById(Long requestId) {
        return assetRequestDAO.findAssetRequestByIdForUpdate(requestId);
    }
}

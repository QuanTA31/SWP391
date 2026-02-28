package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetRequestServiceImpl implements AssetRequestService {

    private final AssetRequestDAO assetRequestDAO;

    @Override
    public Long createPurchaseRequestForm(AssetRequest assetRequest) {
        assetRequestDAO.insert(assetRequest);
        return assetRequestDAO.getLastId();
    }

    @Override
    public Optional<AssetRequest> findAssetRequestByIdForUpdate(Long assetRequestId) {
        return assetRequestDAO.findAssetRequestByIdForUpdate(assetRequestId);
    }

    @Override
    public void updatePurchaseRequest(AssetRequest assetRequest) {
        assetRequestDAO.update(assetRequest);
    }

    @Override
    public void updatePurchaseRequestStatus(AssetRequest assetRequest) {
        assetRequestDAO.updateStatus(assetRequest);
    }

    @Override
    public Integer countById(Long assetRequestId, String status) {
        return assetRequestDAO.countById(assetRequestId, status);
    }

    @Override
    public AssetRequest findByUpdate(Long assetRequestId) {
        return assetRequestDAO.selectByUpdate(assetRequestId);
    }

    @Override
    public int updateIsSelected(AssetRequest assetRequest) {
        return assetRequestDAO.updateIsSelected(assetRequest);
    }
}

package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetRequestServiceImpl implements AssetRequestService {

    private final AssetRequestDAO assetRequestDAO;

    @Override
    public Long createPurchaseRequestForm(AssetRequest assetRequest) {
        assetRequestDAO.insert(assetRequest);
        return assetRequest.id;
    }
}

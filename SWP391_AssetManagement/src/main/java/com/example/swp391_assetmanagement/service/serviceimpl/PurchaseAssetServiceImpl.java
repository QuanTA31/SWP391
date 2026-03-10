package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.PurchaseAssetListDAO;
import com.example.swp391_assetmanagement.service.PurchaseAssetListService;
import com.example.swp391_assetmanagement.service.servicerequest.PurchaseAssetAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.PurchaseAssetAllServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseAssetServiceImpl implements PurchaseAssetListService {

    private final PurchaseAssetListDAO purchaseAssetListDAO;

    @Override
    public List<PurchaseAssetAllServiceResponse> viewPurchaseAssetList(PurchaseAssetAllServiceRequest purchaseAssetAllServiceRequest) {
        List<PurchaseAssetAllServiceResponse> daoResponses = purchaseAssetListDAO.selectPurchaseAssetAll(purchaseAssetAllServiceRequest);

        if (daoResponses.isEmpty()) {
            return Collections.emptyList();
        }

        return daoResponses;
    }
}

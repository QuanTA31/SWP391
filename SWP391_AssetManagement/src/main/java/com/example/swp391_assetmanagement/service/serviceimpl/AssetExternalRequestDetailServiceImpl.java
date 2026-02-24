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
    public int[] createPurchaseRequest(List<AssetExternalRequestDetail> details) {

        return assetExternalRequestDetailDAO.insert(details);
    }
}

package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;

import java.util.List;

public interface AssetExternalRequestDetailService {

    int[] createPurchaseRequest(List<AssetExternalRequestDetail> details);
}

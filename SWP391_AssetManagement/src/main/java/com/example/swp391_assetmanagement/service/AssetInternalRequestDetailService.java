package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetForRepairServiceResponse;

import java.util.List;

public interface AssetInternalRequestDetailService {

    int insert(AssetInternalRequestDetail detail);

    List<AssetForRepairServiceResponse> findAssetsByLocationId(String locationId);

    AssetInternalRequestDetail findByAssetRequestId(Long assetRequestId);

    int update(AssetInternalRequestDetail detail);
    // Hàm này để insert từng cái hoặc dùng cho loop
    void createDetail(AssetInternalRequestDetail detail);

    // Nếu bạn muốn tối ưu (giống code mẫu dùng batchInsert)
    void batchInsert(List<AssetInternalRequestDetail> details);
}

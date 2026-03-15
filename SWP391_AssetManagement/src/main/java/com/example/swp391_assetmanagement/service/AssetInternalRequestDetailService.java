package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;

import java.util.List;

public interface AssetInternalRequestDetailService {
    // Hàm này để insert từng cái hoặc dùng cho loop
    void createDetail(AssetInternalRequestDetail detail);

    // Nếu bạn muốn tối ưu (giống code mẫu dùng batchInsert)
    void batchInsert(List<AssetInternalRequestDetail> details);
}

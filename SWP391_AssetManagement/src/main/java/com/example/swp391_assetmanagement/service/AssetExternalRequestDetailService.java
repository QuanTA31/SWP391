package com.example.swp391_assetmanagement.service;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;

import java.util.List;

public interface AssetExternalRequestDetailService {

    int[] batchInsert(List<AssetExternalRequestDetail> details);

    List<AssetExternalRequestDetail> getByAssetRequestId(Long assetRequestId);

    int[] batchUpdate(List<AssetExternalRequestDetail> details);

    List<AssetExternalRequestDetail> getByAssetRequestIdForUpdate(Long assetRequestId);

    void batchDelete(List<Long> idsToDelete);

    AssetExternalRequestDetail findToUpdate(Long id);

    Long findAssetRequest(Long assetRequestDetailId);

    Integer countOptionDetail(Long assetRequestId);

    void updateExternalStatusId(Long id, String externalStatusId);

}

package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetExternalRequestDetailDAO;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetExternalRequestDetailServiceResponse;
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

    @Override
    public Long findAssetRequest(Long assetRequestDetailId) {
        return assetExternalRequestDetailDAO.findAssetRequestId(assetRequestDetailId);
    }

    @Override
    public Integer countOptionDetail(Long assetRequestId) {
        return assetExternalRequestDetailDAO.countOptionDetail(assetRequestId);
    }

    @Override
    public void updateExternalStatusId(Long id, String externalStatusId) {
        assetExternalRequestDetailDAO.updateExternalStatusId(id, externalStatusId);
    }

    @Override
    public List<AssetExternalRequestDetailServiceResponse>  findByAssetRequestId(Long assetRequestId) {
        return assetExternalRequestDetailDAO.findByAssetRequestId(assetRequestId);
    }

    @Override
    public Long insert(AssetExternalRequestDetail details) {
        assetExternalRequestDetailDAO.insert(details);
        return assetExternalRequestDetailDAO.getLastId();
    }
}

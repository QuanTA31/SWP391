package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dao.InventoryDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.InventoryService;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryProcessServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryActionServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetInternalRequestDetailServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryDAO inventoryDAO;
    private final AssetRequestDAO assetRequestDAO;

    @Override
    public Long insertRequest(AssetRequest entity) {
        inventoryDAO.insertInventoryRequest(entity);
        // Sau khi insert, gọi hàm lấy ID vừa sinh ra
        return assetRequestDAO.getLastId();
    }

    @Override
    public int insertDetail(AssetInternalRequestDetail entity) {
        return inventoryDAO.insertInventoryDetail(entity);
    }

    @Override
    public List<InventoryItemServiceResponse> selectItems(InventoryProcessServiceRequest request) {
        return inventoryDAO.selectInventoryItems(request);
    }

    @Override
    public List<Assets> findByLocationAndStatuses(String locationId, java.util.List<String> statusIds) {
        return inventoryDAO.selectAssetsByLocationAndStatuses(locationId, statusIds);
    }

    @Override
    public int countUnfinishedItems(InventoryActionServiceRequest request) {
        return inventoryDAO.countUnfinishedInventoryItems(request);
    }

    @Override
    public int updateRequest(AssetRequest entity) {
        return inventoryDAO.updateInventoryRequest(entity);
    }

    @Override
    public int updateDetail(AssetInternalRequestDetail entity) {
        return inventoryDAO.updateInventoryDetail(entity);
    }

    @Override
    public int updateAssetStatus(Assets assets) {
        return inventoryDAO.updateAssetStatus(assets);
    }

    @Override
    public int updateAssetStatusAndNote(Assets assets) {
        return inventoryDAO.updateAssetStatusAndNote(assets);
    }

    @Override
    public List<AssetInternalRequestDetailServiceResponse> selectAllDetails(InventoryActionServiceRequest request) {
        return inventoryDAO.selectAllDetailsByRequestId(request);
    }
}

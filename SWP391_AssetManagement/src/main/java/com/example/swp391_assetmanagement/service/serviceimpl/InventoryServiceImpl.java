package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dao.InventoryDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.InventoryService;
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
    public List<InventoryItemServiceResponse> selectItems(Long requestId, String assetTypeId, String fullName) {
        return inventoryDAO.selectInventoryItems(requestId, assetTypeId, fullName);
    }

    @Override
    public int countUnfinishedItems(Long requestId) {
        return inventoryDAO.countUnfinishedInventoryItems(requestId);
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
}

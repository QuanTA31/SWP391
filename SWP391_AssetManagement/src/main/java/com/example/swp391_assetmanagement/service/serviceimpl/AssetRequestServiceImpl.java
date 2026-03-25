package com.example.swp391_assetmanagement.service.serviceimpl;

import com.example.swp391_assetmanagement.dao.AssetInternalRequestDetailDAO;
import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dao.AssetsDAO;
import com.example.swp391_assetmanagement.dao.InventoryDAO;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.servicerequest.RecoverServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetRequestServiceImpl implements AssetRequestService {

    private final AssetRequestDAO assetRequestDAO;
    private final AssetInternalRequestDetailDAO detailDAO;
    private final AssetsDAO assetsDAO;
    private final InventoryDAO inventoryDAO;

    @Override
    public String findRequestTypeById(Long assetRequestId) {
        return assetRequestDAO.findRequestTypeById(assetRequestId);
    }

    @Override
    public Long createPurchaseRequestForm(AssetRequest assetRequest) {
        assetRequestDAO.insert(assetRequest);
        return assetRequestDAO.getLastId();
    }

    @Override
    public Optional<AssetRequest> findAssetRequestByIdForUpdate(Long assetRequestId) {
        return assetRequestDAO.findAssetRequestByIdForUpdate(assetRequestId);
    }

    @Override
    public void updatePurchaseRequest(AssetRequest assetRequest) {
        assetRequestDAO.update(assetRequest);
    }

    @Override
    public void updateLiquidationRequest(AssetRequest assetRequest) {
        assetRequestDAO.update(assetRequest);
    }

    @Override
    public void updatePurchaseRequestStatus(AssetRequest assetRequest) {
        assetRequestDAO.updateStatus(assetRequest);
    }

    @Override
    public Integer countById(Long assetRequestId, String status) {
        return assetRequestDAO.countById(assetRequestId, status);
    }

    @Override
    public AssetRequest findByUpdate(Long assetRequestId) {
        return assetRequestDAO.selectByUpdate(assetRequestId);
    }

    @Override
    public int updateIsSelected(AssetRequest assetRequest) {
        return assetRequestDAO.updateIsSelected(assetRequest);
    }

    @Override
    public Long findIdByAssetRequestDetailId(Long assetRequestDetailId) {
        return assetRequestDAO.findIdByAssetRequestDetailId(assetRequestDetailId);
    }

    @Override
    public int moveInProgress(Long requestId) {
        return assetRequestDAO.moveInProgress(requestId, RequestStatus.RESEARCH_DONE.getValue(),
                RequestStatus.IN_PROGRESS.getValue());
    }

    @Override
    public int moveCompleted(Long requestId) {
        return assetRequestDAO.moveCompleted(requestId, RequestStatus.IN_PROGRESS.getValue(),
                RequestStatus.COMPLETED.getValue());
    }

    @Override
    public void updateAssetsToDisposed(Long requestId, AssetStatus status) {
        if (requestId == null || status == null) return;

        int updated = assetsDAO.updateStatusByRequestId(
                requestId,
                status.getValue()
        );
        if (updated == 0) {
            throw new RuntimeException("No asset updated");
        }
    }

    //Retrival

    @Override
    public AssetRequest findById(Long id) {
        return assetRequestDAO.selectById(id);
    }

    @Override
    public List<AssetInternalRequestDetail> findDetailsByRequestId(Long requestId) {
        return detailDAO.selectByRequestId(requestId);
    }

    @Override
    public void confirmDetailAndRestoreAsset(RecoverServiceRequest serviceRequest) {
        // 1. Truy vấn Entity
        AssetInternalRequestDetail detail = detailDAO.selectById(serviceRequest.getDetailId());

        // 2. Truy cập trực tiếp field (không dùng get)
        if (detail != null && Boolean.FALSE.equals(detail.isDone)) {
            detail.isDone = true;
            detailDAO.updateIsDone(detail);

            Assets asset = assetsDAO.selectById(detail.assetId);
            if (asset != null) {
                asset.assetStatusId = serviceRequest.getTargetStatus();
                asset.locationId = serviceRequest.getTargetLocation();
                asset.currentUserId = null; // Clear người dùng cũ
                assetsDAO.updateRecovery(asset);
            }
        }
    }

    @Override
    public String getRequestStatusById(Long requestId) {
        return assetRequestDAO.getStatusById(requestId);
    }

    @Override
    public void updateRequestStatus(Long requestId, String statusId) {
        assetRequestDAO.updateStatusById(requestId, statusId);
    }

    @Override
    public boolean isAllDetailsDone(Long requestId) {
        return detailDAO.countRemainingItems(requestId) == 0;
    }

    @Override
    public void updateHandoverDate(Long requestId, LocalDate handoverDate) {
        // Thực thi cập nhật ngày bàn giao vào DB
        assetRequestDAO.updateHandoverDate(requestId, handoverDate);
    }

    @Override
    public AssetInternalRequestDetail findDetailById(Long detailId) {
        return inventoryDAO.selectDetailById(detailId);
    }
}

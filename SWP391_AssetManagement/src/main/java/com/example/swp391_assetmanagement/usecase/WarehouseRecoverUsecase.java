package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.RecoverItemDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.servicerequest.RecoverServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseRecoverUsecase {

    private final AssetRequestService assetRequestService;

    public AssetRequest getRequestInfo(Long requestId) {
        return assetRequestService.findById(requestId);
    }

    public List<AssetInternalRequestDetail> getRequestDetails(Long requestId) {
        return assetRequestService.findDetailsByRequestId(requestId);
    }

    @Transactional
    public void prepareProcessing(Long requestId) {
        String currentStatus = assetRequestService.getRequestStatusById(requestId);
        if ("03".equals(currentStatus)) { // APPROVED
            assetRequestService.updateRequestStatus(requestId, "05"); // IN_PROGRESS
        }
    }

    @Transactional
    public void executeRecovery(Long detailId, Long requestId) {
        RecoverServiceRequest serviceReq = RecoverServiceRequest.builder()
                .detailId(detailId)
                .targetStatus("08") // STOCKED
                .targetLocation("05")  // WAREHOUSE
                .build();

        assetRequestService.confirmDetailAndRestoreAsset(serviceReq);

        if (assetRequestService.isAllDetailsDone(requestId)) {
            assetRequestService.updateRequestStatus(requestId, "06"); // COMPLETED
        }
    }
}
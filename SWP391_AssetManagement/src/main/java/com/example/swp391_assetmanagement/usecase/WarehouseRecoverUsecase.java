package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.RecoverItemDTORequest;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.servicerequest.RecoverServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WarehouseRecoverUsecase {

    private final AssetRequestService assetRequestService;

    @Transactional
    public void prepareProcessing(Long requestId) {
        // Chỉ lấy status, không lấy Entity
        String currentStatus = assetRequestService.getRequestStatusById(requestId);
        if ("03".equals(currentStatus)) { // APPROVED
            assetRequestService.updateRequestStatus(requestId, "05"); // IN_PROGRESS
        }
    }

    @Transactional
    public void executeRecovery(Long detailId, Long requestId) {
        // Tạo Service Request (DTO nội bộ của Service)
        RecoverServiceRequest serviceReq = RecoverServiceRequest.builder()
                .detailId(detailId)
                .targetStatus("08") // STOCKED
                .targetLocation("05")  // WAREHOUSE
                .build();

        // Gọi Service xử lý
        assetRequestService.confirmDetailAndRestoreAsset(serviceReq);

        // Kiểm tra logic đóng Request tổng
        if (assetRequestService.isAllDetailsDone(requestId)) {
            assetRequestService.updateRequestStatus(requestId, "06"); // COMPLETED
        }
    }
}

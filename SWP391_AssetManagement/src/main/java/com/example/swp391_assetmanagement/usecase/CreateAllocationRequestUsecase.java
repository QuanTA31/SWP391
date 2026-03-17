package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.UserDAO;
import com.example.swp391_assetmanagement.dto.request.AllocationDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AllocationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateAllocationRequestUsecase {

    private final AllocationService allocationService;
    private final UserDAO userDAO;

    @Transactional
    public void execute(AllocationDTORequest dto, HttpSession session) {

        // Validate required fields when creating (not draft)
        if (!"draft".equalsIgnoreCase(dto.getAction())) {
            if (dto.getAssetTypeId() == null || dto.getAssetTypeId().trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn loại tài sản.");
            }
            if (dto.getQuantity() == null || dto.getQuantity() < 1) {
                throw new IllegalArgumentException("Vui lòng nhập số lượng (tối thiểu 1).");
            }
            if (dto.getReason() == null || dto.getReason().trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập lý do yêu cầu.");
            }
        }

        String statusId = "draft".equalsIgnoreCase(dto.getAction())
                ? RequestStatus.DRAFT.getValue()
                : RequestStatus.PENDING_APPROVAL.getValue();

        String requesterCode = (String) session.getAttribute("USER_CODE");
        Long requestedBy = userDAO.findIdByUserCode(requesterCode);

        if (dto.getAssetRequestId() == null) {
            // == CREATE NEW ==
            AssetRequest assetRequest = new AssetRequest();
            assetRequest.requestTypeId = RequestType.ALLOCATION.getValue();
            assetRequest.requestStatusId = statusId;
            assetRequest.requestedBy = requestedBy;
            assetRequest.requestedDate = LocalDate.now();
            assetRequest.note = dto.getReason();
            assetRequest.createdAt = LocalDateTime.now();

            Long assetRequestId = allocationService.createAssetRequest(assetRequest);

            AssetInternalRequestDetail detail = new AssetInternalRequestDetail();
            detail.assetRequestId = assetRequestId;
            detail.assetId = dto.getAssetId();
            detail.assetTypeId = dto.getAssetTypeId();
            detail.note = dto.getReason();
            detail.createdAt = LocalDateTime.now();
            detail.toLocationId = dto.getLocationId();
            detail.quantity = dto.getQuantity();

            allocationService.createInternalDetail(detail);

        } else {
            // == UPDATE EXISTING DRAFT ==
            AssetRequest assetRequest = allocationService.getAssetRequestById(dto.getAssetRequestId()).orElse(null);
            if (assetRequest == null) throw new IllegalArgumentException("Không tìm thấy Request");
            
            assetRequest.requestStatusId = statusId;
            assetRequest.note = dto.getReason();
            allocationService.updateAssetRequest(assetRequest);

            AssetInternalRequestDetail detail = allocationService.getInternalDetailByRequestId(dto.getAssetRequestId());
            boolean isNewDetail = false;
            if (detail == null) {
                detail = new AssetInternalRequestDetail();
                detail.assetRequestId = dto.getAssetRequestId();
                detail.createdAt = LocalDateTime.now();
                isNewDetail = true;
            }

            detail.assetId = dto.getAssetId();
            detail.assetTypeId = dto.getAssetTypeId();
            detail.note = dto.getReason();

            // Reset location vs user fields before re-populating to switch cleanly
            detail.toLocationId = null;
            detail.toUserId = null;

            detail.toLocationId = dto.getLocationId();
            detail.quantity = dto.getQuantity();

            if (isNewDetail) {
                allocationService.createInternalDetail(detail);
            } else {
                allocationService.updateInternalDetail(detail);
            }
        }
    }
}

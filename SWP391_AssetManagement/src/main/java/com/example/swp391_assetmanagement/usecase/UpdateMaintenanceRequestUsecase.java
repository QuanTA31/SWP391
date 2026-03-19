package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AssetRequestDAO;
import com.example.swp391_assetmanagement.dto.request.CreateMaintenanceRequestDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetInternalRequestDetailService;
import com.example.swp391_assetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UpdateMaintenanceRequestUsecase {

    private final AssetRequestDAO assetRequestDAO;
    private final AssetInternalRequestDetailService assetInternalRequestDetailService;
    private final UserService userService;

    @Transactional
    public void execute(Long assetRequestId, CreateMaintenanceRequestDTORequest request, String userCode) {
        
        // 1. Validate owner and status
        AssetRequest assetRequest = assetRequestDAO.findAssetRequestByIdForUpdate(assetRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu"));

        Long userId = userService.getIdByUserCode(userCode);
        if (!assetRequest.requestedBy.equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền sửa yêu cầu này!");
        }

        if (!RequestStatus.DRAFT.getValue().equals(assetRequest.requestStatusId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chỉ được sửa yêu cầu ở trạng thái NHÁP.");
        }

        // 2. Validate input
        if (request.getAssetId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hãy chọn tài sản");
        }
        if (request.getIssueDescription() == null || request.getIssueDescription().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập mô tả lỗi");
        }
        if (request.getPriority() == null || request.getPriority().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn mức độ ưu tiên");
        }

        // 3. Update AssetRequest header nếu submit
        if (Boolean.TRUE.equals(request.getIsSubmitted())) {
            assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue()); // Bypass PENDING
            assetRequest.setRequestedDate(LocalDate.now());
            assetRequestDAO.updateStatus(assetRequest);
            assetRequestDAO.update(assetRequest);
        }

        // 4. Update Detail
        AssetInternalRequestDetail detail = assetInternalRequestDetailService.findByAssetRequestId(assetRequestId);
        if (detail == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lỗi dữ liệu: không tìm thấy chi tiết yêu cầu");
        }

        detail.setAssetId(request.getAssetId());

        String combinedNote = String.format("[%s] %s | Note: %s",
            request.getPriority(),
            request.getIssueDescription().trim(),
            (request.getNote() != null ? request.getNote().trim() : ""));

        detail.setNote(combinedNote);

        assetInternalRequestDetailService.update(detail);
    }
}

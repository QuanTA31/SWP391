package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.AssetsDAO;
import com.example.swp391_assetmanagement.dto.request.CreateMaintenanceRequestDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetInternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateMaintenanceRequestUsecase {

    private final AssetRequestService assetRequestService;
    private final AssetInternalRequestDetailService assetInternalRequestDetailService;
    private final UserService userService;
    private final AssetsDAO assetsDAO;

    @Transactional
    public void execute(CreateMaintenanceRequestDTORequest request, HttpSession session) {

        // Validate
        if (request.getAssetId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn tài sản cần sửa chữa!");
        }
        if (ObjectUtils.isEmpty(request.getIssueDescription())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng nhập mô tả lỗi!");
        }
        if (ObjectUtils.isEmpty(request.getPriority())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn mức độ ưu tiên!");
        }

        Long userId = userService.getIdByUserCode(session.getAttribute("USER_CODE").toString());

        String statusId = Boolean.TRUE.equals(request.getIsSubmitted())
                ? RequestStatus.APPROVED.getValue()
                : RequestStatus.DRAFT.getValue();

        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setRequestTypeId(RequestType.MAINTENANCE.getValue());
        assetRequest.setRequestedBy(userId);
        assetRequest.setRequestedDate(LocalDate.now());
        assetRequest.setRequestStatusId(statusId);

        Long assetRequestId = assetRequestService.createPurchaseRequestForm(assetRequest);

        // Lấy location hiện tại của asset để lưu vào fromLocationId
        Assets asset = assetsDAO.findById(request.getAssetId());
        if (asset == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài sản!");
        }

        String noteContent = buildNote(request);

        AssetInternalRequestDetail detail = new AssetInternalRequestDetail();
        detail.setAssetId(request.getAssetId());
        detail.setAssetRequestId(assetRequestId);
        detail.setAssetTypeId("");
        detail.setQuantity(1);
        detail.setFromLocationId(asset.locationId); // Lưu location gốc để restore sau khi sửa xong
        detail.setNote(noteContent);
        detail.setIsDone(false);
        detail.setCreatedAt(LocalDateTime.now());

        assetInternalRequestDetailService.insert(detail);
    }


    private String buildNote(CreateMaintenanceRequestDTORequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(request.getPriority()).append("] ");
        sb.append(request.getIssueDescription().trim());
        if (!ObjectUtils.isEmpty(request.getNote())) {
            sb.append(" | Note: ").append(request.getNote().trim());
        }
        return sb.toString();
    }
}

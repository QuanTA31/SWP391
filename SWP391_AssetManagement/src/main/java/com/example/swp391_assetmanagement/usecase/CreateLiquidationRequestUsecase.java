package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreateLiquidationDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.ExternalStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreateLiquidationRequestUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;
    private final UserService userService;

    @Transactional
    public void execute(CreateLiquidationDTORequest request, HttpSession session) {

        if (CollectionUtils.isEmpty(request.getCreateLiquidationDetailDTORequestList())) {
            throw new ValidationException("Danh sách tài sản thanh lý không được rỗng");
        }

        Object userCodeObj = session.getAttribute("USER_CODE");
        if (userCodeObj == null) {
            throw new ValidationException("Session không hợp lệ");
        }

        Long userId = userService.getIdByUserCode(userCodeObj.toString());

        if (request.getAssetRequestId() == null) {

            AssetRequest assetRequest = new AssetRequest();
            assetRequest.setRequestTypeId(RequestType.LIQUIDATION.getValue());
            assetRequest.setRequestedBy(userId);
            assetRequest.setRequestedDate(LocalDate.now());
            assetRequest.setRequestStatusId(
                    Boolean.TRUE.equals(request.getIsSubmitted())
                            ? RequestStatus.PENDING_APPROVAL.getValue()
                            : RequestStatus.DRAFT.getValue()
            );

            Long requestId = assetRequestService.createPurchaseRequestForm(assetRequest);

            List<AssetExternalRequestDetail> details =
                    request.getCreateLiquidationDetailDTORequestList()
                            .stream()
                            .map(dto -> {
                                AssetExternalRequestDetail entity = new AssetExternalRequestDetail();

                                entity.setAssetRequestId(requestId);
                                entity.setAssetTypeId(dto.getAssetTypeId());
                                entity.setQuantity(dto.getQuantity());
                                entity.setNote(dto.getNote());
                                entity.setExternalStatusId(ExternalStatus.DRAFT.getValue());

                                return entity;
                            })
                            .toList();

            assetExternalRequestDetailService.batchInsert(details);
        }
    }
}
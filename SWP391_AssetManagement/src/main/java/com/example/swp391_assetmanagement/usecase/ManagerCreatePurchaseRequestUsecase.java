package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ApprovalPurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.enums.ExternalStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManagerCreatePurchaseRequestUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;
    private final UserService userService;

    @Transactional
    public void execute(ApprovalPurchaseRequestDTORequest request, HttpSession session) {

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(request.getAssetRequestId());

        if (ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.PROCUREMENT.getValue())) {
            throw new ValidationException();
        }

        Integer countRequest = assetRequestService.countById(request.getAssetRequestId(), RequestStatus.PENDING_APPROVAL.getValue());

        if (countRequest == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is invalid!");
        }

        Long userId = userService.getIdByUserCode(session.getAttribute("USER_CODE").toString());

        // Get list assetExternalRequestDetail from DB
        List<AssetExternalRequestDetail> dbDetails =
                assetExternalRequestDetailService.getByAssetRequestIdForUpdate(request.getAssetRequestId());

        // Update assetExternalRequestDetail
        List<AssetExternalRequestDetail> toUpdate = dbDetails.stream()
                .peek(dto -> dto.setExternalStatusId(request.getIsApproved()
                        ? ExternalStatus.IN_PROGRESS.getValue() : ExternalStatus.CANCEL.getValue())).toList();
        assetExternalRequestDetailService.batchUpdate(toUpdate);

        // Update AssetRequest if status = submit
        assetRequestService.findAssetRequestByIdForUpdate(request.getAssetRequestId()).ifPresent(
                assetRequest -> {
                    assetRequest.setApprovedBy(userId);
                    assetRequest.setRequestStatusId(request.getIsApproved()
                            ? RequestStatus.APPROVED.getValue() : RequestStatus.CANCELLED.getValue());
                    assetRequest.setNote(request.getNote());
                    assetRequestService.updatePurchaseRequest(assetRequest);
                }
        );

    }
}

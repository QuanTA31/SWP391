package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ApprovalPurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.enums.ExternalStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerCreatePurchaseRequestUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;
    private final UserService userService;

    @Transactional
    public void execute(ApprovalPurchaseRequestDTORequest request, HttpSession session) {

        // Get list assetExternalRequestDetail from DB
        List<AssetExternalRequestDetail> dbDetails =
                assetExternalRequestDetailService.getByAssetRequestIdForUpdate(request.getAssetRequestId());

        // Update assetExternalRequestDetail
        List<AssetExternalRequestDetail> toUpdate = dbDetails.stream()
                .map(dto -> {
                    AssetExternalRequestDetail entity = new AssetExternalRequestDetail();
                    entity.setExternalStatusId(request.isApproved()
                            ? ExternalStatus.IN_PROGRESS.getValue() : ExternalStatus.CANCEL.getValue());
                    return entity;
                }).toList();
        assetExternalRequestDetailService.batchUpdate(toUpdate);

        // Update AssetRequest if status = submit
        assetRequestService.findAssetRequestByIdForUpdate(request.getAssetRequestId()).ifPresent(
                assetRequest -> {
                        assetRequest.setRequestStatusId(request.isApproved()
                                ? RequestStatus.APPROVED.getValue() : RequestStatus.COMPLETED.getValue());
                        assetRequest.setNote(request.getNote());
                        assetRequestService.updatePurchaseRequest(assetRequest);
                }
        );

    }
}

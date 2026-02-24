package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.ExternalStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatePurchaseRequestUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;
    private final UserService userService;

    public void execute(CreatePurchaseRequestDTORequest request, HttpSession session) {

        Long userId = userService.getIdByUserCode(session.getAttribute("USER_CODE").toString());

        AssetRequest assetRequest = new AssetRequest();

        assetRequest.setRequestTypeId(RequestType.PROCUREMENT.getValue());
        assetRequest.setRequestedBy(userId);
        assetRequest.setRequestedDate(LocalDate.now());
        assetRequest.setRequestStatusId(
                request.isSubmitted()
                        ? RequestStatus.PENDING_APPROVAL.getValue()
                        : RequestStatus.DRAFT.getValue()
        );

        Long assetRequestId =
                assetRequestService.createPurchaseRequestForm(assetRequest);

        List<AssetExternalRequestDetail> details =
                request.getCreatePurchaseRequestDetailDTORequestList()
                        .stream()
                        .map(dto -> {

                            AssetExternalRequestDetail detail = new AssetExternalRequestDetail();

                            detail.setAssetRequestId(assetRequestId);
                            detail.setAssetTypeId(AssetType.of(dto.getAssetTypeId()).getValue());
                            detail.setExternalStatusId(
                                    request.isSubmitted()
                                            ? ExternalStatus.IN_PROGRESS.getValue()
                                            : ExternalStatus.DRAFT.getValue());
                            detail.setNote(dto.getNote());

                            return detail;
                        })
                        .toList();
    }
}

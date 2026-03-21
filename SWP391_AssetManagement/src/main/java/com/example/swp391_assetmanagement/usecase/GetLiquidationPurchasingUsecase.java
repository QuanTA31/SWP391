package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreateLiquidationDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreateLiquidationDetailDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLiquidationPurchasingUsecase {
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public CreateLiquidationDTORequest execute(Long assetRequestId) {

        CreateLiquidationDTORequest.CreateLiquidationDTORequestBuilder
                createLiquidationDTORequest = CreateLiquidationDTORequest.builder();

        if(assetRequestId == null) {
            createLiquidationDTORequest.createLiquidationDetailDTORequestList(Collections.emptyList());
            createLiquidationDTORequest.isSubmitted(false);
            createLiquidationDTORequest.assetRequestId(null);
            createLiquidationDTORequest.requestStatus(null);
            return createLiquidationDTORequest.build();
        }

        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId).ifPresent(assetRequest -> {
            createLiquidationDTORequest.requestStatus(assetRequest.requestStatusId);
        });

        List<AssetExternalRequestDetail> details = assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreateLiquidationDetailDTORequest> detailDTOs = details.stream()
                .map(detail -> CreateLiquidationDetailDTORequest.builder()
                        .assetExternalRequestDetailId(detail.id)
                        .assetTypeId(detail.assetTypeId)
                        .assetTypeName(AssetType.of(detail.assetTypeId).getName())
                        .externalStatusId(detail.externalStatusId)
                        .quantity(detail.quantity)
                        .note(detail.note)
                        .build())
                .toList();
        createLiquidationDTORequest.assetRequestId(assetRequestId);
        createLiquidationDTORequest.createLiquidationDetailDTORequestList(detailDTOs);
        createLiquidationDTORequest.isSubmitted(!RequestStatus.DRAFT.getValue()
                .equals(createLiquidationDTORequest.build().getRequestStatus()));
        return createLiquidationDTORequest.build();
    }
}

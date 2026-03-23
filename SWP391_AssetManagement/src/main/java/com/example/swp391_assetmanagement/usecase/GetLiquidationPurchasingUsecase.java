package com.example.swp391_assetmanagement.usecase;


import com.example.swp391_assetmanagement.dto.response.CreateLiquidationDTOResponse;
import com.example.swp391_assetmanagement.dto.response.CreateLiquidationDetailDTOResponse;
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

    public CreateLiquidationDTOResponse execute(Long assetRequestId) {

        CreateLiquidationDTOResponse.CreateLiquidationDTOResponseBuilder response =
                CreateLiquidationDTOResponse.builder();

        if (assetRequestId == null) {
            return response
                    .details(Collections.emptyList())
                    .isSubmitted(false)
                    .assetRequestId(null)
                    .requestStatus(null)
                    .build();
        }

        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId)
                .ifPresent(assetRequest -> response.requestStatus(assetRequest.requestStatusId));

        List<AssetExternalRequestDetail> details =
                assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreateLiquidationDetailDTOResponse> detailDTOs = details.stream()
                .map(detail -> CreateLiquidationDetailDTOResponse.builder()
                        .assetExternalRequestDetailId(detail.id)
                        .assetTypeId(detail.assetTypeId)
                        .assetTypeName(AssetType.of(detail.assetTypeId).getName())
                        .externalStatusId(detail.externalStatusId)
                        .quantity(detail.quantity)
                        .note(detail.note)
                        .build())
                .toList();

        String requestStatus = response.build().getRequestStatus();

        return response
                .assetRequestId(assetRequestId)
                .details(detailDTOs)
                .isSubmitted(!String.valueOf(RequestStatus.DRAFT.getValue()).equals(requestStatus))
                .build();
    }
}
package com.example.swp391_assetmanagement.usecase;


import com.example.swp391_assetmanagement.dto.response.CreateLiquidationDetailDTOResponse;
import com.example.swp391_assetmanagement.dto.response.GetLiquidationManagerDTOResponse;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLiquidationManagerUsecase {
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public GetLiquidationManagerDTOResponse execute(Long assetRequestId) {

        GetLiquidationManagerDTOResponse.GetLiquidationManagerDTOResponseBuilder builder =
                GetLiquidationManagerDTOResponse.builder();

        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId).ifPresent(assetRequest -> {
            builder.requestStatus(assetRequest.requestStatusId);
            builder.requestTypeId(assetRequest.requestTypeId);
            builder.requestedDate(assetRequest.requestedDate);
        });

        List<AssetExternalRequestDetail> details =
                assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreateLiquidationDetailDTOResponse> detailsDTOs = details.stream()
                .map(detail -> CreateLiquidationDetailDTOResponse.builder()
                        .assetExternalRequestDetailId(detail.id)
                        .assetTypeId(detail.assetTypeId)
                        .assetTypeName(AssetType.of(detail.assetTypeId).getName())
                        .externalStatusId(detail.externalStatusId)
                        .quantity(detail.quantity)
                        .note(detail.note)
                        .build())
                .toList();

        return builder
                .assetRequestId(assetRequestId)
                .details(detailsDTOs)
                .isSubmitted(true)
                .build();
    }
}

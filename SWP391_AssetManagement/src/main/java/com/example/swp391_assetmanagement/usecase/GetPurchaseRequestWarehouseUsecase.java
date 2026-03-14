package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDetailDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPurchaseRequestWarehouseUsecase {
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public CreatePurchaseRequestDTORequest execute(Long assetRequestId) {

        CreatePurchaseRequestDTORequest.CreatePurchaseRequestDTORequestBuilder
                createPurchaseRequestDTORequest = CreatePurchaseRequestDTORequest.builder();

        if(assetRequestId == null) {
            createPurchaseRequestDTORequest.createPurchaseRequestDetailDTORequestList(Collections.emptyList());
            createPurchaseRequestDTORequest.isSubmitted(false);
            createPurchaseRequestDTORequest.assetRequestId(null);
            createPurchaseRequestDTORequest.requestStatus(null);
            return createPurchaseRequestDTORequest.build();
        }

        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId).ifPresent(assetRequest -> {
            createPurchaseRequestDTORequest.requestStatus(assetRequest.requestStatusId);
        });

        List<AssetExternalRequestDetail> details = assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreatePurchaseRequestDetailDTORequest> detailDTOs = details.stream()
                .map(detail -> CreatePurchaseRequestDetailDTORequest.builder()
                        .assetExternalRequestDetailId(detail.id)
                        .assetTypeId(detail.getAssetTypeId())
                        .assetTypeName(AssetType.of(detail.getAssetTypeId()).getName())
                        .externalStatusId(detail.getExternalStatusId())
                        .quantity(detail.getQuantity())
                        .note(detail.getNote())
                        .build())
                .toList();
        createPurchaseRequestDTORequest.setAssetRequestId(assetRequestId);
        createPurchaseRequestDTORequest.setCreatePurchaseRequestDetailDTORequestList(detailDTOs);
        createPurchaseRequestDTORequest.setIsSubmitted(!RequestStatus.DRAFT.getValue()
                .equals(createPurchaseRequestDTORequest.getRequestStatus()));
        return createPurchaseRequestDTORequest.build();
    }
}

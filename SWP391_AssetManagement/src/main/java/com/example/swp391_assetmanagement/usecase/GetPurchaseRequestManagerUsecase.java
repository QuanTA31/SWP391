package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDetailDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPurchaseRequestManagerUsecase {
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public CreatePurchaseRequestDTORequest execute(Long assetRequestId) {

        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = new CreatePurchaseRequestDTORequest();

        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId).ifPresent(assetRequest -> {
            createPurchaseRequestDTORequest.setRequestStatus(assetRequest.getRequestStatusId());
        });
        List<AssetExternalRequestDetail> details = assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreatePurchaseRequestDetailDTORequest> detailsDTOs = details.stream()
                .map(detail -> CreatePurchaseRequestDetailDTORequest.builder()
                        .assetExternalRequestDetailId(detail.getId())
                        .assetTypeId(detail.getAssetTypeId())
                        .assetTypeName(AssetType.of(detail.getAssetTypeId()).getName())
                        .externalStatusId(detail.getExternalStatusId())
                        .quantity(detail.getQuantity())
                        .note(detail.getNote())
                        .build())
                .toList();

        createPurchaseRequestDTORequest.setAssetRequestId(assetRequestId);
        createPurchaseRequestDTORequest.setCreatePurchaseRequestDetailDTORequestList(detailsDTOs);
        createPurchaseRequestDTORequest.setIsSubmitted(true);
        return createPurchaseRequestDTORequest;
    }
}

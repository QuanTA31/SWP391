package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDetailDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPurchaseRequestManagerUsecase {
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    public CreatePurchaseRequestDTORequest execute(Long assetRequestId) {
        List<AssetExternalRequestDetail> details = assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreatePurchaseRequestDetailDTORequest> detailsDTOs = details.stream()
                .map(detail -> CreatePurchaseRequestDetailDTORequest.builder()
                        .assetExternalRequestDetailId(detail.getId())
                        .assetTypeId(detail.getAssetTypeId())
                        .quantity(detail.getQuantity())
                        .note(detail.getNote())
                        .build())
                .toList();
        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = new CreatePurchaseRequestDTORequest();
        createPurchaseRequestDTORequest.setAssetRequestId(assetRequestId);
        createPurchaseRequestDTORequest.setCreatePurchaseRequestDetailDTORequestList(detailsDTOs);
        createPurchaseRequestDTORequest.setSubmitted(true);
        return createPurchaseRequestDTORequest;
    }
}

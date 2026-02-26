package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDetailDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPurchaseRequestWarehouseUsecase {
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;

    public CreatePurchaseRequestDTORequest execute(Long assetRequestId) {
        CreatePurchaseRequestDTORequest createPurchaseRequestDTORequest = new CreatePurchaseRequestDTORequest();
        if(assetRequestId == null) {
            createPurchaseRequestDTORequest.setCreatePurchaseRequestDetailDTORequestList(new ArrayList<>());
            createPurchaseRequestDTORequest.setSubmitted(false);
            createPurchaseRequestDTORequest.setAssetRequestId(null);
            return createPurchaseRequestDTORequest;
        }
        List<AssetExternalRequestDetail> details = assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreatePurchaseRequestDetailDTORequest> detailDTOs = details.stream()
                .map(detail -> CreatePurchaseRequestDetailDTORequest.builder()
                        .assetExternalRequestDetailId(detail.getId())
                        .assetTypeId(detail.getAssetTypeId())
                        .quantity(detail.getQuantity())
                        .note(detail.getNote())
                        .build())
                .toList();
        createPurchaseRequestDTORequest.setAssetRequestId(assetRequestId);
        createPurchaseRequestDTORequest.setCreatePurchaseRequestDetailDTORequestList(detailDTOs);
        createPurchaseRequestDTORequest.setSubmitted(false);
        return createPurchaseRequestDTORequest;
    }
}

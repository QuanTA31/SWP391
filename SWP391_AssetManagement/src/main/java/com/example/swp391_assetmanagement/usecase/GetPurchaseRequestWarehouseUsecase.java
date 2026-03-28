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

        //Lấy ra trạng thái hiện tại của đơn hàng
        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId).ifPresent(assetRequest -> {
            createPurchaseRequestDTORequest.requestStatus(assetRequest.requestStatusId);
        });

        //lấy danh sách các mặt hàng cần mua
        List<AssetExternalRequestDetail> details = assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreatePurchaseRequestDetailDTORequest> detailDTOs = details.stream()
                .map(detail -> CreatePurchaseRequestDetailDTORequest.builder()
                        .assetExternalRequestDetailId(detail.id)
                        .assetTypeId(detail.assetTypeId)
                        .assetTypeName(AssetType.of(detail.assetTypeId).getName())
                        .externalStatusId(detail.externalStatusId)
                        .quantity(detail.quantity)
                        .note(detail.note)
                        .build())
                .toList();
        createPurchaseRequestDTORequest.assetRequestId(assetRequestId);
        createPurchaseRequestDTORequest.createPurchaseRequestDetailDTORequestList(detailDTOs);
        createPurchaseRequestDTORequest.isSubmitted(!RequestStatus.DRAFT.getValue()
                .equals(createPurchaseRequestDTORequest.build().getRequestStatus()));
        return createPurchaseRequestDTORequest.build();
    }
}

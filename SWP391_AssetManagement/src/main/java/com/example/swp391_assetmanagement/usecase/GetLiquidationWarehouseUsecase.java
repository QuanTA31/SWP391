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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetLiquidationWarehouseUsecase {
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public CreateLiquidationDTORequest execute(Long assetRequestId) {
        CreateLiquidationDTORequest createLiquidationDTORequest = new CreateLiquidationDTORequest();
        if(assetRequestId == null) {
            createLiquidationDTORequest.setCreateLiquidationDetailDTORequestList(new ArrayList<>());
            createLiquidationDTORequest.setIsSubmitted(false);
            createLiquidationDTORequest.setAssetRequestId(null);
            createLiquidationDTORequest.setRequestStatus(null);
            return createLiquidationDTORequest;
        }

        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId).ifPresent(assetRequest -> {
            createLiquidationDTORequest.setRequestStatus(assetRequest.getRequestStatusId());
            createLiquidationDTORequest.setRequestTypeId(assetRequest.getRequestTypeId());
            createLiquidationDTORequest.setRequestedDate(assetRequest.getRequestedDate());
        });

        List<AssetExternalRequestDetail> details = assetExternalRequestDetailService.getByAssetRequestId(assetRequestId);

        List<CreateLiquidationDetailDTORequest> detailDTOs = details.stream()
                .map(detail -> CreateLiquidationDetailDTORequest.builder()
                        .assetExternalRequestDetailId(detail.getId())
                        .assetTypeId(detail.getAssetTypeId())
                        .assetTypeName(AssetType.of(detail.getAssetTypeId()).getName())
                        .externalStatusId(detail.getExternalStatusId())
                        .quantity(detail.getQuantity())
                        .note(detail.getNote())
                        .build())
                .toList();
        createLiquidationDTORequest.setAssetRequestId(assetRequestId);
        createLiquidationDTORequest.setCreateLiquidationDetailDTORequestList(detailDTOs);
        createLiquidationDTORequest.setIsSubmitted(!RequestStatus.DRAFT.getValue()
                .equals(createLiquidationDTORequest.getRequestStatus()));
        return createLiquidationDTORequest;
    }
}

package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.InventoryConfirmDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.InventoryService;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryConfirmServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmInventoryItemUsecase {
    private final InventoryService inventoryService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(InventoryConfirmDTORequest dtoRequest) {
        InventoryConfirmServiceRequest serviceRequest = InventoryConfirmServiceRequest.builder()
                .detailId(dtoRequest.getDetailId())
                .isDone(dtoRequest.getIsDone())
                .note(dtoRequest.getNote())
                .build();

        AssetInternalRequestDetail detail = assetRequestService.findDetailById(serviceRequest.getDetailId());
        detail.setIsDone(serviceRequest.getIsDone());
        detail.setNote(serviceRequest.getNote());
        inventoryService.updateDetail(detail);
    }
}

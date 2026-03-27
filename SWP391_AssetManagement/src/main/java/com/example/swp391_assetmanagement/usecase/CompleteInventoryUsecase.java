package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteInventoryUsecase {
    private final InventoryService inventoryService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(com.example.swp391_assetmanagement.dto.request.InventoryCompleteDTORequest dtoRequest) {
        Long requestId = dtoRequest.getRequestId();
        
        if (dtoRequest.getItems() != null) {
            for (com.example.swp391_assetmanagement.dto.request.InventoryCompleteDTORequest.Item item : dtoRequest.getItems()) {
                com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail detail = assetRequestService.findDetailById(item.getDetailId());
                detail.setIsDone(item.getIsDone());
                detail.setNote(item.getNote());
                inventoryService.updateDetail(detail);
            }
        }

        AssetRequest header = assetRequestService.findById(requestId);
        if ("03".equals(header.requestStatusId)) {
            header.setRequestStatusId("05"); // IN_PROGRESS
            inventoryService.updateRequest(header);
        }
    }
}

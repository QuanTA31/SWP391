package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.InventoryService;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryActionServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetInternalRequestDetailServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateAssetStatusInventoryUsecase {
    private final InventoryService inventoryService;
    private final AssetRequestService assetRequestService;
    private final AssetService assetService;

    @Transactional
    public void execute(Long requestId) {
        InventoryActionServiceRequest serviceRequest = InventoryActionServiceRequest.builder()
                .requestId(requestId)
                .build();

        List<AssetInternalRequestDetailServiceResponse> details = inventoryService.selectAllDetails(serviceRequest);
        
        for (AssetInternalRequestDetailServiceResponse detail : details) {
            if (Boolean.FALSE.equals(detail.getIsDone())) {
                Assets asset = assetService.findById(detail.getAssetId());
                asset.setAssetStatusId("07"); // LOST
                asset.setNote(detail.getNote());
                inventoryService.updateAssetStatusAndNote(asset);
            }
        }

        AssetRequest header = assetRequestService.findById(requestId);
        header.setRequestStatusId("06"); // COMPLETED
        header.setHandoverDate(LocalDate.now());
        inventoryService.updateRequest(header);
    }
}

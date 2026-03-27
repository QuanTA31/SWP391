package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.InventoryProcessDTORequest;
import com.example.swp391_assetmanagement.dto.response.FiltersDTOResponse;
import com.example.swp391_assetmanagement.dto.response.InventoryItemDTOResponse;
import com.example.swp391_assetmanagement.dto.response.InventoryProcessDTOResponse;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.InventoryService;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewInventoryDetailUsecase {
    private final InventoryService inventoryService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public InventoryProcessDTOResponse execute(InventoryProcessDTORequest dtoRequest) {
        InventoryProcessServiceRequest serviceRequest = InventoryProcessServiceRequest.builder()
                .requestId(dtoRequest.getRequestId())
                .assetTypeId(dtoRequest.getAssetTypeId())
                .fullName(dtoRequest.getFullName())
                .build();

        AssetRequest header = assetRequestService.findById(serviceRequest.getRequestId());
        List<InventoryItemServiceResponse> items = inventoryService.selectItems(serviceRequest);

        return InventoryProcessDTOResponse.builder()
                .requestId(header.id)
                .statusName(header.requestStatusId)
                .items(items.stream().map(item ->
                        InventoryItemDTOResponse.builder()
                                .detailId(item.getDetailId())
                                .assetCode(item.getAssetCode())
                                .userFullName(item.getUserFullName())
                                .assetTypeName(AssetType.of(item.getAssetTypeId()).getName())
                                .isDone(item.getIsDone())
                                .statusId(item.getStatusId())
                                .dbStatusName(item.getDbStatusId() != null ? AssetStatus.of(item.getDbStatusId()).getName() : null)
                                .locationName(item.getLocationId() != null ? Location.of(item.getLocationId()).getName() : null)
                                .warrantyPeriod(item.getWarrantyPeriod())
                                .note(item.getNote())
                                .build()
                ).toList())
                .filters(FiltersDTOResponse.builder()
                        .assetTypeId(dtoRequest.getAssetTypeId())
                        .searchWord(dtoRequest.getFullName())
                        .build())
                .build();
    }
}

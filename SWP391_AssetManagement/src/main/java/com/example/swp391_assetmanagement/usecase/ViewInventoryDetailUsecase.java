package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.InventoryProcessDTORequest;
import com.example.swp391_assetmanagement.dto.response.InventoryItemDTOResponse;
import com.example.swp391_assetmanagement.dto.response.InventoryProcessDTOResponse;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.InventoryService;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.seasar.doma.jdbc.SelectOptions;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewInventoryDetailUsecase {
    private final InventoryService inventoryService;
    private final AssetRequestService assetRequestService;
    private final Integer PAGE_SIZE = 10;

    @Transactional
    public InventoryProcessDTOResponse execute(InventoryProcessDTORequest dtoRequest) {
        int pageIndex = (dtoRequest.getPageIndex() != null && dtoRequest.getPageIndex() != 0) ? dtoRequest.getPageIndex() : 1;
        long offset = (long) (pageIndex - 1) * PAGE_SIZE;

        InventoryProcessServiceRequest serviceRequest = InventoryProcessServiceRequest.builder()
                .requestId(dtoRequest.getRequestId())
                .assetTypeId(dtoRequest.getAssetTypeId())
                .fullName(dtoRequest.getFullName())
                .offset(offset)
                .pageSize(PAGE_SIZE)
                .build();

        AssetRequest header = assetRequestService.findById(serviceRequest.getRequestId());
        
        int totalItems = inventoryService.countItems(serviceRequest);
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        SelectOptions options = SelectOptions.get().offset((int) offset).limit(PAGE_SIZE);
        List<InventoryItemServiceResponse> items = inventoryService.selectItems(serviceRequest, options);

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
                                .locationName(item.getLocationId() != null ? com.example.swp391_assetmanagement.enums.Location.of(item.getLocationId()).getName() : null)
                                .warrantyPeriod(item.getWarrantyPeriod())
                                .note(item.getNote())
                                .build()
                ).toList())
                .filters(com.example.swp391_assetmanagement.dto.response.FiltersDTOResponse.builder()
                        .assetTypeId(dtoRequest.getAssetTypeId())
                        .searchWord(dtoRequest.getFullName())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }
}

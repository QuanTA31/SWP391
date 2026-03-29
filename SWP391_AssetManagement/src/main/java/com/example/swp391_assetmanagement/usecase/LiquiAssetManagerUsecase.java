package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.LiquiDateCreateDTORequest;
import com.example.swp391_assetmanagement.dto.response.*;
import com.example.swp391_assetmanagement.enums.*;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.LiquiAssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.LiquiAssetViewAllServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LiquiAssetManagerUsecase {

    private static final Integer PAGE_SIZE = 15;

    private final AssetService assetService;

    @Transactional(readOnly = true)
    public LiquiDateCreateDTOResponse execute(LiquiDateCreateDTORequest request, HttpSession session) {

        validateAssetRequest(request);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0) ? request.getPageIndex() : 1;

        // Get data from database (có limit assetStatus 01 08 05 new stocked broken)
        List<LiquiAssetViewAllServiceResponse> serviceResponses = assetService.liquiViewAllAsset(
                LiquiAssetViewAllServiceRequest.builder()
                        .locationId(request.getLocationId())
                        .assetTypeId(request.getAssetTypeId())
                        .assetStatusId(request.getAssetStatusId())
                        .searchWord(ObjectUtils.isEmpty(request.getSearchWord()) ? null : request.getSearchWord().trim())
                        .offset((pageIndex - 1) * PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .assetRequestId(request.getAssetRequestId())
                        .build());

        if (serviceResponses.isEmpty()) {
            return LiquiDateCreateDTOResponse.builder()
                    .assetResponses(Collections.emptyList())
                    .filters(LiquidateFiltersDTOResponse.builder()
                            .locationId(request.getLocationId())
                            .assetTypeId(request.getAssetTypeId())
                            .assetStatusId(request.getAssetStatusId())
                            .searchWord(request.getSearchWord())
                            .page(pageIndex)
                            .pageSize(PAGE_SIZE)
                            .totalItems(0)
                            .totalPages(1)
                            .hasNextPage(false)
                            .hasPreviousPage(false)
                            .build())
                    .build();
        }

        // lay total asset
        int totalItems = serviceResponses.stream().findFirst().get().getTotalItems();

        // tinh tong so trang
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return LiquiDateCreateDTOResponse.builder()
                .assetResponses(
                        serviceResponses.stream().map(
                                        entity -> LiquidateAssetDTOResponse.builder()
                                                .assetId(entity.assetId)
                                                .assetCode(entity.assetCode)
                                                .describe(entity.description)
                                                .originalPrice(entity.originalPrice)
                                                .warrantyPeriod(entity.warrantyPeriod)
                                                .receivedDate(entity.receivedDate)
                                                .locationName(Location.of(entity.locationId).getName())
                                                .assetStatusName(AssetStatus.of(entity.assetStatusId).getName())
                                                .currentUserId(entity.currentUserId)
                                                .assetTypeName(AssetType.of(entity.assetTypeId).getName())
                                                .isSelected(!Objects.isNull(entity.isSelected) && entity.isSelected)
                                                .build())
                                .toList()
                )
                .filters(LiquidateFiltersDTOResponse.builder()
                        .locationId(request.getLocationId())
                        .assetTypeId(request.getAssetTypeId())
                        .assetStatusId(request.getAssetStatusId())
                        .searchWord(request.getSearchWord())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }

    // ===== VALIDATE ENUM =====
    private void validateAssetRequest(LiquiDateCreateDTORequest request) {

        //Check enums
        if (!ObjectUtils.isEmpty(request.getLocationId()) && !Location.hasValue(request.getLocationId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location is invalid !");
        }
        if (!ObjectUtils.isEmpty(request.getAssetTypeId()) && !AssetType.hasValue(request.getAssetTypeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset Type is invalid !");
        }
        if (!ObjectUtils.isEmpty(request.getAssetStatusId()) && !AssetStatus.hasValue(request.getAssetStatusId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset Status is invalid !");
        }
    }
}

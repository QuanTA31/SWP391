package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewAssetRequest;
import com.example.swp391_assetmanagement.dto.response.AssetResponse;
import com.example.swp391_assetmanagement.dto.response.FiltersResponse;
import com.example.swp391_assetmanagement.dto.response.ViewAllAssetResponse;
import com.example.swp391_assetmanagement.enums.*;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerUsecase {

    private final Integer PAGE_SIZE = 15;

    private final AssetService assetService;

    @Transactional(readOnly = true)
    public ViewAllAssetResponse viewAsset(ViewAssetRequest request, HttpSession session) {

        validateAssetRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0)  ? request.getPageIndex() : 1;

        // Get data from database
        List<AssetViewAllResponse> serviceResponses = assetService.viewAllAsset(
                AssetViewAllRequest.builder()
                        .locationId(request.getLocationId())
                        .assetTypeId(request.getAssetTypeId())
                        .assetStatusId(request.getAssetStatusId())
                        .offset((pageIndex-1)*PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        if (serviceResponses.isEmpty()) {
            return ViewAllAssetResponse.builder()
                    .assetResponses(Collections.emptyList())
                    .filters(FiltersResponse.builder()
                            .locationId(request.getLocationId())
                            .assetTypeId(request.getAssetTypeId())
                            .assetStatusId(request.getAssetStatusId())
                            .page(pageIndex)
                            .pageSize(PAGE_SIZE)
                            .totalItems(0)
                            .totalPages(1)
                            .hasNextPage(false)
                            .hasPreviousPage(false)
                            .build())
                    .build();
        }

        int totalItems = serviceResponses.stream().findFirst().get().getTotalItems();

        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return ViewAllAssetResponse.builder()
                .assetResponses(
                        serviceResponses.stream().map(
                                        entity -> AssetResponse.builder()
                                                .assetCode(entity.assetCode)
                                                .description(entity.description)
                                                .originalPrice(entity.originalPrice)
                                                .warrantyPeriod(entity.warrantyPeriod)
                                                .receivedDate(entity.receivedDate)
                                                .locationName(Location.of(entity.locationId).getName())
                                                .assetStatusName(AssetStatus.of(entity.assetStatusId).getName())
                                                .currentUserId(entity.currentUserId)
                                                .assetTypeName(AssetType.of(entity.assetTypeId).getName())
                                                .build())
                                .toList()
                )
                .filters(FiltersResponse.builder()
                        .locationId(request.getLocationId())
                        .assetTypeId(request.getAssetTypeId())
                        .assetStatusId(request.getAssetStatusId())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }

    private void validateAssetRequest(ViewAssetRequest request, HttpSession session) {

        // Check role
//        if (!Objects.equals(session.getAttribute("ROLE"), Roles.MANAGER.getValue())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này !");
//        }

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

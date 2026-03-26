package com.example.swp391_assetmanagement.usecase;


import com.example.swp391_assetmanagement.dto.request.ViewAssetDTORequest;
import com.example.swp391_assetmanagement.dto.response.AssetDTOResponse;
import com.example.swp391_assetmanagement.dto.response.FiltersDTOResponse;
import com.example.swp391_assetmanagement.dto.response.ViewAllAssetDTOResponse;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetViewAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetViewAllServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.LocationViewAssetServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViewAssetUsecase {

    private final Integer PAGE_SIZE = 15;

    private final AssetService assetService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public ViewAllAssetDTOResponse viewAsset(ViewAssetDTORequest request, HttpSession session) {

        validateAssetRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0) ? request.getPageIndex() : 1;

        //Check location, phân asset theo role
        ArrayList<String> locationIdList = new ArrayList<>();

        if (Objects.equals(session.getAttribute("ROLE").toString(), Roles.MANAGER.getValue())
                || Objects.equals(session.getAttribute("ROLE"), Roles.WAREHOUSE.getValue())) {

            locationIdList.add(Location.HEAD_OFFICE.getValue());
            locationIdList.add(Location.BRANCH_OFFICE.getValue());
            locationIdList.add(Location.MEETING_ROOM.getValue());
            locationIdList.add(Location.IT_ROOM.getValue());
            locationIdList.add(Location.WAREHOUSE.getValue());

        } else if (Objects.equals(session.getAttribute("ROLE"), Roles.DEPARTMENT_MANAGER.getValue())) {

            LocationViewAssetServiceResponse locationViewAssetResponse = userService.getLocationViewAsset(
                    session.getAttribute("USER_CODE").toString());
            if (Location.hasValue(locationViewAssetResponse.locationId)) {
                locationIdList.add(Location.of(locationViewAssetResponse.locationId).getValue());
            }
        }

        // Get data from database
        List<AssetViewAllServiceResponse> serviceResponses = assetService.viewAllAsset(
                AssetViewAllServiceRequest.builder()
                        .locationId(request.getLocationId())
                        .assetTypeId(request.getAssetTypeId())
                        .assetStatusId(request.getAssetStatusId())
                        .searchWord(ObjectUtils.isEmpty(request.getSearchWord()) ? null : request.getSearchWord().trim())
                        .locationIdList(locationIdList)
                        .offset((pageIndex - 1) * PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        if (serviceResponses.isEmpty()) {
            return ViewAllAssetDTOResponse.builder()
                    .assetResponses(Collections.emptyList())
                    .filters(FiltersDTOResponse.builder()
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

        int totalItems = serviceResponses.stream().findFirst().get().getTotalItems();

        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return ViewAllAssetDTOResponse.builder()
                .assetResponses(
                        serviceResponses.stream().map(
                                        entity -> AssetDTOResponse.builder()
                                                .assetCode(entity.assetCode)
                                                .describe(entity.description)
                                                .originalPrice(entity.originalPrice)
                                                .warrantyPeriod(entity.warrantyPeriod)
                                                .receivedDate(entity.receivedDate)
                                                .locationName(Location.of(entity.locationId).getName())
                                                .assetStatusName(AssetStatus.of(entity.assetStatusId).getName())
                                                .currentUserId(entity.currentUserId)
                                                .currentUserName(entity.currentUserName)
                                                .assetTypeName(AssetType.of(entity.assetTypeId).getName())
                                                .build())
                                .toList()
                )
                .filters(FiltersDTOResponse.builder()
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

    private void validateAssetRequest(ViewAssetDTORequest request, HttpSession session) {

        // Check role
        if (Objects.equals(session.getAttribute("ROLE"), Roles.ADMIN.getValue())
                //|| Objects.equals(session.getAttribute("ROLE"), Roles.DEPARTMENT_MANAGER.getValue())
                || Objects.equals(session.getAttribute("ROLE"), Roles.CLIENT.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này !");
        }

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

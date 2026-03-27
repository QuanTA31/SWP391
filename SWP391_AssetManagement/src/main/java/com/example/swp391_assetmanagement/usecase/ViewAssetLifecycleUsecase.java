package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewAssetLifecycleDTORequest;
import com.example.swp391_assetmanagement.dto.response.AssetLifecycleFilterDTOResponse;
import com.example.swp391_assetmanagement.dto.response.AssetLifecycleRequestDTOResponse;
import com.example.swp391_assetmanagement.dto.response.ViewAssetLifecycleDTOResponse;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.servicerequest.AssetLifecycleServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.AssetLifecycleServiceResponse;
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
public class ViewAssetLifecycleUsecase {

    private static final Integer PAGE_SIZE = 15;

    private final AssetService assetService;

    @Transactional(readOnly = true)
    public ViewAssetLifecycleDTOResponse viewAssetLifecycle(ViewAssetLifecycleDTORequest request, HttpSession session) {

        validateRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0) ? request.getPageIndex() : 1;

        // Lấy thông tin asset
        Assets asset = assetService.findByAssetCode(request.getAssetCode());
        if (asset == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Asset not found: " + request.getAssetCode());
        }

        // Gọi service để lấy danh sách requests liên quan
        List<AssetLifecycleServiceResponse> serviceResponses = assetService.viewAssetLifecycle(
                AssetLifecycleServiceRequest.builder()
                        .assetCode(request.getAssetCode())
                        .requestTypeId(ObjectUtils.isEmpty(request.getRequestTypeId()) ? null : request.getRequestTypeId().trim())
                        .offset((pageIndex - 1) * PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        // Build asset header info
        String assetTypeName = AssetType.hasValue(asset.assetTypeId) ? AssetType.of(asset.assetTypeId).getName() : asset.assetTypeId;
        String assetStatusName = AssetStatus.hasValue(asset.assetStatusId) ? AssetStatus.of(asset.assetStatusId).getName() : asset.assetStatusId;
        String locationName = Location.hasValue(asset.locationId) ? Location.of(asset.locationId).getName() : asset.locationId;

        // Empty case
        if (serviceResponses.isEmpty()) {
            return ViewAssetLifecycleDTOResponse.builder()
                    .assetCode(asset.assetCode)
                    .assetTypeName(assetTypeName)
                    .assetStatusName(assetStatusName)
                    .locationName(locationName)
                    .receivedDate(asset.receivedDate)
                    .originalPrice(asset.originalPrice)
                    .description(asset.description)
                    .requests(Collections.emptyList())
                    .filters(AssetLifecycleFilterDTOResponse.builder()
                            .assetCode(request.getAssetCode())
                            .requestTypeId(request.getRequestTypeId())
                            .page(pageIndex)
                            .pageSize(PAGE_SIZE)
                            .totalItems(0)
                            .totalPages(1)
                            .hasNextPage(false)
                            .hasPreviousPage(false)
                            .build())
                    .build();
        }

        int totalItems = serviceResponses.stream().findFirst().get().totalItems;
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        List<AssetLifecycleRequestDTOResponse> requestDTOs = serviceResponses.stream()
                .map(r -> AssetLifecycleRequestDTOResponse.builder()
                        .requestId(r.requestId)
                        .requestTypeName(RequestType.hasValue(r.requestTypeId) ? RequestType.of(r.requestTypeId).getName() : r.requestTypeId)
                        .requestStatusName(RequestStatus.hasValue(r.requestStatusId) ? RequestStatus.of(r.requestStatusId).getName() : r.requestStatusId)
                        .requestedDate(r.requestedDate)
                        .requestedByName(r.requestedByName)
                        .approvedDate(r.approvedDate)
                        .approvedByName(r.approvedByName)
                        .handoverDate(r.handoverDate)
                        .note(r.note)
                        .build())
                .toList();

        return ViewAssetLifecycleDTOResponse.builder()
                .assetCode(asset.assetCode)
                .assetTypeName(assetTypeName)
                .assetStatusName(assetStatusName)
                .locationName(locationName)
                .receivedDate(asset.receivedDate)
                .originalPrice(asset.originalPrice)
                .description(asset.description)
                .requests(requestDTOs)
                .filters(AssetLifecycleFilterDTOResponse.builder()
                        .assetCode(request.getAssetCode())
                        .requestTypeId(request.getRequestTypeId())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }

    private void validateRequest(ViewAssetLifecycleDTORequest request, HttpSession session) {
        // Chỉ MANAGER mới được xem lifecycle
        if (Objects.equals(session.getAttribute("ROLE"), Roles.PURCHASING.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này !");
        }

        // assetCode bắt buộc
        if (ObjectUtils.isEmpty(request.getAssetCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset code is required !");
        }

        // Validate requestTypeId nếu có
        if (!ObjectUtils.isEmpty(request.getRequestTypeId()) && !RequestType.hasValue(request.getRequestTypeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request type is invalid !");
        }
    }
}

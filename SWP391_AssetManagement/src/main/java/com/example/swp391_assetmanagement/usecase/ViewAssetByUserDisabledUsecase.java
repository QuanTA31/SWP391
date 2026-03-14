package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewAssetByUserDisabledDTORequest;
import com.example.swp391_assetmanagement.dto.response.ViewAssetByUserDisabledDTOResponse;
import com.example.swp391_assetmanagement.enums.Location;
import com.example.swp391_assetmanagement.service.ViewAssetByUserDisabledService;
import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetByUserDisabledServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetByUserDisabledServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewAssetByUserDisabledUsecase {
    private final Integer PAGE_SIZE = 15;
    private final ViewAssetByUserDisabledService service;

    @Transactional(readOnly = true)
    public ViewAssetByUserDisabledDTOResponse viewAssetDisabled(ViewAssetByUserDisabledDTORequest request, HttpSession session) {
        // 1. Validate quyền truy cập (Admin/Manager mới được xem chẳng hạn)
        validateAccess(session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() > 0) ? request.getPageIndex() : 1;

        // 2. Map request sang ServiceRequest
        ViewAssetByUserDisabledServiceRequest serviceRequest = ViewAssetByUserDisabledServiceRequest.builder()
                .userStatus("03") // Trạng thái Disable
                .name(request.getName())
                .locationId(request.getLocationId())
                .assetTypeId(request.getAssetTypeId())
                .offset((pageIndex - 1) * PAGE_SIZE)
                .pageSize(PAGE_SIZE)
                .build();

        List<ViewAssetByUserDisabledServiceResponse> serviceResponses = service.selectAllAssetByUserDisable(serviceRequest);

        if (serviceResponses.isEmpty()) {
            return ViewAssetByUserDisabledDTOResponse.builder()
                    .assets(Collections.emptyList())
                    .page(pageIndex).pageSize(PAGE_SIZE).totalAsset(0).totalPages(0)
                    .build();
        }

        // 3. Tính toán phân trang
        int totalAssets = serviceResponses.get(0).getTotalItems();
        int totalPages = (int) Math.ceil((double) totalAssets / PAGE_SIZE);

        // 4. Map Response (Lưu ý: Bạn nên tạo một List bên trong ViewAssetByUserDisabledDTOResponse)
        return ViewAssetByUserDisabledDTOResponse.builder()
                .assets(serviceResponses.stream()
                        .map(this::mapToItemResponse)
                        .toList())
                .page(pageIndex)
                .pageSize(PAGE_SIZE)
                .totalAsset(totalAssets)
                .totalPages(totalPages)
                .hasNextPage(pageIndex < totalPages)
                .hasPreviousPage(pageIndex > 1)
                .build();
    }

    private void validateAccess(HttpSession session) {
        String role = (String) session.getAttribute("ROLE");
        // Nếu là Client hoặc các role thấp thì chặn
        if (List.of("CLIENT", "WAREHOUSE").contains(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền!");
        }
    }

    private ViewAssetByUserDisabledDTOResponse.AssetDetailResponse mapToItemResponse(ViewAssetByUserDisabledServiceResponse entity) {
        return ViewAssetByUserDisabledDTOResponse.AssetDetailResponse.builder()
                .assetCode(entity.getAssetCode())
                .description(entity.getDescription())
                .receivedDate(entity.getReceivedDate())
                .locationName(Location.of(entity.getLocationId()).getName())
                .assetStatusId(entity.getAssetStatusId())
                .assetTypeId(entity.getAssetTypeId())
                .username(entity.getUsername())
                .userStatus(entity.getUserStatus())
                .build();
    }
}


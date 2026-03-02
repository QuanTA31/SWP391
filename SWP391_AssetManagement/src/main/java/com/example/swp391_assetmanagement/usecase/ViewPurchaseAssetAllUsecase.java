package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewPurchaseAssetDTORequest;
import com.example.swp391_assetmanagement.dto.response.FilterPurchaseAssetDTOResponse;
import com.example.swp391_assetmanagement.dto.response.PurchaseAssetDTOResponse;
import com.example.swp391_assetmanagement.dto.response.ViewPurchaseAssetAllDTOResponse;
import com.example.swp391_assetmanagement.enums.AssetStatus;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.PurchaseAssetListService;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.PurchaseAssetAllServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.PurchaseAssetAllServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViewPurchaseAssetAllUsecase {

    private final Integer PAGE_SIZE = 15;

    private final PurchaseAssetListService purchaseAssetListService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public ViewPurchaseAssetAllDTOResponse viewPurchaseAssetAllDTOResponse (ViewPurchaseAssetDTORequest request, HttpSession session) {
        validatePurchaseAssetRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0)
                ? request.getPageIndex()
                : 1;

        // Gọi service
        List<PurchaseAssetAllServiceResponse> serviceResponses =
                purchaseAssetListService.viewPurchaseAssetList(
                        PurchaseAssetAllServiceRequest.builder()
                                .assetRequestId(request.getAssetRequestId())
                                .assetTypeId(request.getAssetTypeId())
                                .searchWord(
                                        request.getSearchWord() == null || request.getSearchWord().trim().isEmpty()
                                                ? null
                                                : request.getSearchWord().trim()
                                )
                                .offset((pageIndex - 1) * PAGE_SIZE)
                                .pageSize(PAGE_SIZE)
                                .build()
                );

        if (serviceResponses.isEmpty()) {
            return ViewPurchaseAssetAllDTOResponse.builder()
                    .purchaseAssetDTOResponses(List.of())
                    .filterPurchaseAssetDTOResponse(
                            FilterPurchaseAssetDTOResponse.builder()
                                    .assetTypeId(request.getAssetTypeId())
                                    .searchWord(request.getSearchWord())
                                    .page(pageIndex)
                                    .pageSize(PAGE_SIZE)
                                    .totalItems(0)
                                    .totalPages(1)
                                    .hasNextPage(false)
                                    .hasPreviousPage(false)
                                    .build()
                    )
                    .build();
        }

        int totalItems = serviceResponses.get(0).getTotalItems();
        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);

        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return ViewPurchaseAssetAllDTOResponse.builder()
                .purchaseAssetDTOResponses(
                        serviceResponses.stream()
                                .map(entity ->
                                        PurchaseAssetDTOResponse.builder()
                                                .assetCode(entity.assetCode)
                                                .assetTypeName(AssetType.of(entity.assetTypeId).getName())
                                                .warrantyPeriod(entity.warrantyPeriod)
                                                .receivedDate(entity.warrantyPeriod)
                                                .originalPrice(entity.originalPrice)
                                                .description(entity.description)
                                                .receivedDate(entity.receivedDate)
                                                .build()
                                )
                                .toList()
                )
                .filterPurchaseAssetDTOResponse(
                        FilterPurchaseAssetDTOResponse.builder()
                                .assetTypeId(request.getAssetTypeId())
                                .searchWord(request.getSearchWord())
                                .page(pageIndex)
                                .pageSize(PAGE_SIZE)
                                .totalItems(totalItems)
                                .totalPages(totalPages)
                                .hasNextPage(hasNext)
                                .hasPreviousPage(hasPrevious)
                                .build()
                )
                .build();
    }

    private void validatePurchaseAssetRequest(ViewPurchaseAssetDTORequest request, HttpSession session) {

        // Check role
        if (!Objects.equals(session.getAttribute("ROLE"), Roles.WAREHOUSE.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này !");
        }

    }

}

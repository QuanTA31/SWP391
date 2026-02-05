package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListRequest;
import com.example.swp391_assetmanagement.dto.response.OptionDetailListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOptionDetailListUseCase {

    private static final int PAGE_SIZE = 10;

    private final OptionDetailService optionDetailService;
    private final AuthGuardService authGuardService;

    public OptionDetailListResponse execute(
            Long requestDetailId,
            String status,
            Integer page
    ) {
        authGuardService.checkManagerOrPurchasing();
        if (!optionDetailService.existsRequestDetail(requestDetailId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Request detail not found: " + requestDetailId
            );
        }
        authGuardService.checkCanAccessRequest(requestDetailId);

        String selectedStatus = (status == null || status.isBlank()) ? "all" : status;
        int pageIndex = (page == null || page < 1) ? 1 : page;
        Boolean isSelected = parseSelectedStatus(selectedStatus);

        OptionDetailListRequest request = OptionDetailListRequest.builder()
                .requestDetailId(requestDetailId)
                .isSelected(isSelected)
                .offset((pageIndex - 1) * PAGE_SIZE)
                .pageSize(PAGE_SIZE)
                .build();

        List<OptionDetail> plans = optionDetailService.getList(request);
        int totalItems = optionDetailService.count(request);

        int totalPages = Math.max(1,
                (int) Math.ceil((double) totalItems / PAGE_SIZE));

        boolean canApprove = authGuardService.canApprove();

        boolean hasAnySelected =
                optionDetailService.countByRequestDetailId(requestDetailId, true) > 0;

        return OptionDetailListResponse.builder()
                .requestDetailId(requestDetailId)
                .plans(plans)
                .status(selectedStatus)
                .page(pageIndex)
                .pageSize(PAGE_SIZE)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .hasPreviousPage(pageIndex > 1)
                .hasNextPage(pageIndex < totalPages)
                .canApprove(canApprove)
                .canManage(true)
                .hasAnySelected(hasAnySelected)
                .build();
    }

    private Boolean parseSelectedStatus(String status) {
        if ("all".equalsIgnoreCase(status)) return null;
        if ("selected".equalsIgnoreCase(status)) return true;
        if ("unselected".equalsIgnoreCase(status)) return false;
        throw new IllegalArgumentException("Invalid status");
    }
}

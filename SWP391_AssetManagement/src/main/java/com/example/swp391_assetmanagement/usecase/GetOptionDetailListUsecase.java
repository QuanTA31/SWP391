package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListRequest;
import com.example.swp391_assetmanagement.dto.response.OptionDetailListDTOResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOptionDetailListUsecase {

    private static final int PAGE_SIZE = 10;

    private final OptionDetailService optionDetailService;

    public OptionDetailListDTOResponse execute(
            Long requestDetailId,
            String status,
            Integer page,
            HttpSession session
    ) {
        // Check role
        String role = (String) session.getAttribute("ROLE");

        if (!Roles.MANAGER.getValue().equals(role)
                && !Roles.PURCHASING.getValue().equals(role)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này"
            );
        }

        // check request detail
        if (!optionDetailService.existsRequestDetail(requestDetailId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Request detail not found: " + requestDetailId
            );
        }

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

        boolean hasAnySelected =
                optionDetailService.countByRequestDetailId(requestDetailId, true) > 0;

        return OptionDetailListDTOResponse.builder()
                .requestDetailId(requestDetailId)
                .plans(plans)
                .status(selectedStatus)
                .page(pageIndex)
                .pageSize(PAGE_SIZE)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .hasPreviousPage(pageIndex > 1)
                .hasNextPage(pageIndex < totalPages)
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
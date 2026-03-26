package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.dto.request.OptionDetailListDTORequest;
import com.example.swp391_assetmanagement.dto.response.OptionDetailListDTOResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.OptionDetailServiceResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetLiquidationOptionListUsecase {

    private static final int PAGE_SIZE = 10;

    private final OptionDetailService optionDetailService;
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public OptionDetailListDTOResponse execute(
            Long requestDetailId,
            String status,
            Integer page,
            HttpSession session
    ) {
        // Get requestId
        Long requestId = assetExternalRequestDetailService.findAssetRequest(requestDetailId);

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(requestId);

        if ((ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.LIQUIDATION.getValue()))) {
            throw new ValidationException("Invalid request type");
        }

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

        OptionDetailListDTORequest request = OptionDetailListDTORequest.builder()
                .requestDetailId(requestDetailId)
                .isSelected(isSelected)
                .offset((pageIndex - 1) * PAGE_SIZE)
                .pageSize(PAGE_SIZE)
                .build();

        List<OptionDetailServiceResponse> plans = optionDetailService.getList(request);
        int totalItems = optionDetailService.count(request);

        int totalPages = Math.max(1,
                (int) Math.ceil((double) totalItems / PAGE_SIZE));

        boolean hasAnySelected =
                optionDetailService.countByRequestDetailId(requestDetailId, true) > 0;

        AssetRequest assetRequest =
                assetRequestService.findByUpdate(requestId);

        boolean isApproved =
                Objects.equals(
                        RequestStatus.APPROVED.getValue(),
                        assetRequest.requestStatusId
                );

        boolean isManager = Roles.MANAGER.getValue().equals(role);
        boolean isPurchasing = Roles.PURCHASING.getValue().equals(role);

        return OptionDetailListDTOResponse.builder()
                .requestDetailId(requestDetailId)
                .assetRequestId(requestId)
                .plans(plans)
                .status(selectedStatus)
                .page(pageIndex)
                .pageSize(PAGE_SIZE)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .hasPreviousPage(pageIndex > 1)
                .hasNextPage(pageIndex < totalPages)
                .canManage(isPurchasing && isApproved && !hasAnySelected)
                .canApprove(isManager)
                .hasAnySelected(hasAnySelected)
                .build();
    }

    private Boolean parseSelectedStatus(String status) {
        if ("all".equalsIgnoreCase(status)) return null;
        if ("selected".equalsIgnoreCase(status)) return true;
        if ("unselected".equalsIgnoreCase(status)) return false;
        throw new IllegalArgumentException("Invalid status");
    }

    public void loadToModel(Long requestDetailId,
                            String status,
                            Integer page,
                            HttpSession session,
                            Model model) {

        model.addAllAttributes(
                this.execute(requestDetailId, status, page, session)
                        .toModel()
        );
    }
}
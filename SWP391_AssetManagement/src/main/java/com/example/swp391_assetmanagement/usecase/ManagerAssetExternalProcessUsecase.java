package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewExternalProcessRequest;
import com.example.swp391_assetmanagement.dto.response.*;
//import com.example.swp391_assetmanagement.enums.ApprovalStatus;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetExternalProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.ExternalProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ExternalProcessAllResponse;
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
public class ManagerAssetExternalProcessUsecase {

    private final Integer PAGE_SIZE = 15;

    private final AssetExternalProcessService assetExternalProcessService;

    @Transactional(readOnly = true)
    public ViewExternalProcessAllResponse viewExternalProcess(ViewExternalProcessRequest request, HttpSession session) {

        validateExternalRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0)  ? request.getPageIndex() : 1;

        // Get data from database
        List<ExternalProcessAllResponse> externalProcessResponses = assetExternalProcessService.viewExternalProcess(
                ExternalProcessRequest.builder()
                        .requestStatusId(request.getRequestStatusId())
                        .requestTypeId(request.getRequestTypeId())
//                        .approvalStatusId(request.getApprovalStatusId())
                        .offset((pageIndex-1)*PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        if (externalProcessResponses.isEmpty()) {
            return ViewExternalProcessAllResponse.builder()
                    .externalProcessResponses(Collections.emptyList())
                    .filters(FilterExternalResponse.builder()
                            .requestStatusId(request.getRequestStatusId())
                            .requestTypeId(request.getRequestTypeId())
//                            .approvalStatusId(request.getApprovalStatusId())
                            .page(pageIndex)
                            .pageSize(PAGE_SIZE)
                            .totalItems(0)
                            .totalPages(1)
                            .hasNextPage(false)
                            .hasPreviousPage(false)
                            .build())
                    .build();
        }

        int totalItems = externalProcessResponses.stream().findFirst().get().getTotalItems();

        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return ViewExternalProcessAllResponse.builder()
                .externalProcessResponses(
                        externalProcessResponses.stream().map(
                                        entity -> ExternalProcessResponse.builder()
//                                                .assetId(entity.assetId)
                                                .assetRequestName(entity.assetRequestId)
                                                .assetTypeName(AssetType.of(entity.assetTypeId).getName())
//                                                .requestStatusName(RequestStatus.of(entity.requestStatusId).getName())
//                                                .requestTypeName(RequestType.of(entity.requestTypeId).getName())
                                                .externalStatusName(entity.externalStatusId)
                                                .quantity(entity.quantity)
//                                                .handoverDate(entity.handoverDate)
                                                .note(entity.note)
                                                .createdAt(entity.createdAt)
                                                //.approvalStatusName(ApprovalStatus.of(entity.approvalStatusId).getName())
//                                                .optionDetailId(entity.optionDetail)
                                                .build())
                                .toList()
                )
                .filters(FilterExternalResponse.builder()
                        .requestStatusId(request.getRequestStatusId())
                        .requestTypeId(request.getRequestTypeId())
//                        .approvalStatusId(request.getApprovalStatusId())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }

    // -----------------Lấy details của từng request----
    @Transactional(readOnly = true)
    public ViewExternalProcessAllResponse getDetailById(Long requestId) {

        // Gọi Service lấy danh sách các item trong phiếu External này
        List<ExternalProcessAllResponse> details = assetExternalProcessService.viewExternalProcess(
                ExternalProcessRequest.builder()
                        .requestId(requestId) // Lọc theo ID phiếu
                        .offset(0)
                        .pageSize(100) // Lấy tối đa 100 dòng
                        .build());

        if (details.isEmpty()) {
            return ViewExternalProcessAllResponse.builder()
                    .externalProcessResponses(Collections.emptyList())
                    .build();
        }

        // Map sang DTO Response
        return ViewExternalProcessAllResponse.builder()
                .externalProcessResponses(
                        details.stream().map(entity -> ExternalProcessResponse.builder()
                                .assetRequestName(entity.assetRequestId)
                                .assetTypeName(AssetType.of(entity.assetTypeId).getName())
                                .externalStatusName(entity.externalStatusId) // Bạn có thể map Enum ExternalStatus ở đây nếu có
                                .quantity(entity.quantity)
                                .note(entity.note)
                                .createdAt(entity.createdAt)
                                .build()
                        ).toList()
                )
                .build();
    }

    private void validateExternalRequest(ViewExternalProcessRequest request, HttpSession session) {

        //  Check role
        if (!Objects.equals(session.getAttribute("ROLE"), Roles.ADMIN.getValue())
        || !Objects.equals(session.getAttribute("ROLE"), Roles.CLIENT.getValue())
        || !Objects.equals(session.getAttribute("ROLE"), Roles.WAREHOUSE.getValue())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này !");
        }

        //Check enums
        if (!ObjectUtils.isEmpty(request.getRequestStatusId()) && !RequestStatus.hasValue(request.getRequestStatusId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status is invalid !");
        }
        if (!ObjectUtils.isEmpty(request.getRequestTypeId()) && !RequestType.hasValue(request.getRequestTypeId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request Type is invalid !");
        }
//        if (!ObjectUtils.isEmpty(request.getApprovalStatusId()) && !ApprovalStatus.hasValue(request.getApprovalStatusId())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Approval Status is invalid !");
//        }
    }

}

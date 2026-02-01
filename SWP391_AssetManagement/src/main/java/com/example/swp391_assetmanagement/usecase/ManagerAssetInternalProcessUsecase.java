package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewInternalProcessRequest;
import com.example.swp391_assetmanagement.dto.response.*;
import com.example.swp391_assetmanagement.enums.*;
import com.example.swp391_assetmanagement.service.AssetInternalProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.InternalProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InternalProcessAllResponse;
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
public class ManagerAssetInternalProcessUsecase {

    private final Integer PAGE_SIZE = 15;

    private final AssetInternalProcessService assetInternalProcessService;

    @Transactional(readOnly = true)
    public ViewInternalProcessAllResponse viewInternalProcess(ViewInternalProcessRequest request, HttpSession session) {

        validateInternalRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0)  ? request.getPageIndex() : 1;

        // Get data from database
        List<InternalProcessAllResponse> internalProcessResponses = assetInternalProcessService.viewInternalProcess(
                InternalProcessRequest.builder()
                        .requestStatusId(request.getRequestStatusId())
                        .requestTypeId(request.getRequestTypeId())
                        .approvalStatusId(request.getApprovalStatusId())
                        .offset((pageIndex-1)*PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        if (internalProcessResponses.isEmpty()) {
            return ViewInternalProcessAllResponse.builder()
                    .internalProcessResponses(Collections.emptyList())
                    .filters(FilterInternalResponse.builder()
                            .requestStatusId(request.getRequestStatusId())
                            .requestTypeId(request.getRequestTypeId())
                            .approvalStatusId(request.getApprovalStatusId())
                            .page(pageIndex)
                            .pageSize(PAGE_SIZE)
                            .totalItems(0)
                            .totalPages(1)
                            .hasNextPage(false)
                            .hasPreviousPage(false)
                            .build())
                    .build();
        }

        int totalItems = internalProcessResponses.stream().findFirst().get().getTotalItems();

        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return ViewInternalProcessAllResponse.builder()
                .internalProcessResponses(
                        internalProcessResponses.stream().map(
                                        entity -> InternalProcessResponse.builder()
                                                .assetId(entity.assetId)
                                                .requestStatusName(RequestStatus.of(entity.requestStatusId).getName())
                                                .requestTypeName(RequestType.of(entity.requestTypeId).getName())
                                                .fromUserId(entity.fromUserId)
                                                .toUserId(entity.toUserId)
                                                .dateOfExecution(entity.dateOfExecution)
                                                .handoverDate(entity.handoverDate)
                                                .note(entity.note)
//                                                .approvalStatusName(ApprovalStatus.of(entity.approvalStatusId).getName())
                                                .build())
                                .toList()
                )
                .filters(FilterInternalResponse.builder()
                        .requestStatusId(request.getRequestStatusId())
                        .requestTypeId(request.getRequestTypeId())
                        .approvalStatusId(request.getApprovalStatusId())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }

    private void validateInternalRequest(ViewInternalProcessRequest request, HttpSession session) {

        // Check role
//        if (!Objects.equals(session.getAttribute("ROLE"), Roles.MANAGER.getValue())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào trang này !");
//        }

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

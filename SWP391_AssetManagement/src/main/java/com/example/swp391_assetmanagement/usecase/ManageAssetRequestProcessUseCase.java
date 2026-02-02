package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewAllProcessRequest;
import com.example.swp391_assetmanagement.dto.response.*;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetAllProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.AllProcessRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllResponse;
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
public class ManageAssetRequestProcessUseCase {

    private final Integer PAGE_SIZE = 15;

    private final AssetAllProcessService assetAllProcessService;

    @Transactional(readOnly = true)
    public ViewAllProcessResponse viewAllProcess(ViewAllProcessRequest request, HttpSession session) {

        validateAllRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0)  ? request.getPageIndex() : 1;

        List<String> requestTypeIdList = new ArrayList<>();

        String role = session.getAttribute("ROLE").toString();

        if (Objects.equals(role, Roles.MANAGER.getValue())) {
            requestTypeIdList.addAll(
                    List.of(
                            RequestType.ALLOCATION.getValue(),
                            RequestType.RETRIEVAL.getValue(),
                            RequestType.PROCUREMENT.getValue(),
                            RequestType.MAINTENANCE.getValue(),
                            RequestType.LIQUIDATION.getValue()
                    )
            );
        }
        else if (Objects.equals(role, Roles.WAREHOUSE.getValue())) {
            requestTypeIdList.addAll(
                    List.of(
                            RequestType.PROCUREMENT.getValue(),
                            RequestType.RETRIEVAL.getValue(),
                            RequestType.ALLOCATION.getValue()
                    )
            );
        }
        else if (Objects.equals(role, Roles.PURCHASING.getValue())) {
            requestTypeIdList.addAll(
                    List.of(
                            RequestType.PROCUREMENT.getValue(),
                            RequestType.LIQUIDATION.getValue(),
                            RequestType.MAINTENANCE.getValue()
                    )
            );
        }else if(Objects.equals(role, Roles.DEPARTMENT_MANAGER.getValue())){
            requestTypeIdList.add(RequestType.ALLOCATION.getValue());
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }


        // Get data from database
        List<RequestProcessAllResponse> allProcessResponses = assetAllProcessService.viewAllProcess(
                AllProcessRequest.builder()
                        .requestStatusId(request.getRequestStatusId())
                        .requestTypeId(request.getRequestTypeId())
                 //       .approvalStatusId(request.getApprovalStatusId())
                        .offset((pageIndex-1)*PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        if (allProcessResponses.isEmpty()) {
            return ViewAllProcessResponse.builder()
                    .allProcessResponses(Collections.emptyList())
                    .filters(FilterAllResponse.builder()
                            .requestStatusId(request.getRequestStatusId())
                            .requestTypeId(request.getRequestTypeId())
                        //    .approvalStatusId(request.getApprovalStatusId())
                            .page(pageIndex)
                            .pageSize(PAGE_SIZE)
                            .totalItems(0)
                            .totalPages(1)
                            .hasNextPage(false)
                            .hasPreviousPage(false)
                            .build())
                    .build();
        }

        int totalItems = allProcessResponses.stream().findFirst().get().getTotalItems();

        int totalPages = (int) Math.ceil((double) totalItems / PAGE_SIZE);
        boolean hasNext = pageIndex < totalPages;
        boolean hasPrevious = pageIndex > 1;

        return ViewAllProcessResponse.builder()
                .allProcessResponses(
                        allProcessResponses.stream().map(
                                        entity -> AllProcessResponse.builder()
                                                .requestTypeName(RequestType.of(entity.requestTypeId).getName())
                                                .requestedBy(entity.requestedBy)
                                                .requestedDate(entity.requestedDate)
                                                .requestStatusName(RequestStatus.of(entity.requestStatusId).getName())
                                                .approvalBy(
                                                        entity.approvalBy != null ? entity.approvalBy : null
                                                )
                                                .approvalDate(entity.approvalDate)
                                                .handoverDate(entity.handoverDate)
                                                .note(entity.note)
                                                .createdAt(entity.createdAt)
                                                .build())
                                .toList()
                )
                .filters(FilterAllResponse.builder()
                        .requestStatusId(request.getRequestStatusId())
                        .requestTypeId(request.getRequestTypeId())
                       // .approvalStatusId(request.getApprovalStatusId())
                        .page(pageIndex)
                        .pageSize(PAGE_SIZE)
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNextPage(hasNext)
                        .hasPreviousPage(hasPrevious)
                        .build())
                .build();
    }


    private void validateAllRequest(ViewAllProcessRequest request, HttpSession session) {

         //Check role
        if (!Objects.equals(session.getAttribute("ROLE"), Roles.ADMIN.getValue())
        || Objects.equals(session.getAttribute("ROLE"), Roles.DEPARTMENT_MANAGER.getValue())
        || Objects.equals(session.getAttribute("ROLE"), Roles.CLIENT.getValue())) {
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

package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewAllProcessDTORequest;
import com.example.swp391_assetmanagement.dto.response.*;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetAllProcessService;
import com.example.swp391_assetmanagement.service.servicerequest.AllProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.RequestProcessAllServiceResponse;
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
public class ManageAssetRequestProcessUsecase {

    private final Integer PAGE_SIZE = 15;

    private final AssetAllProcessService assetAllProcessService;

    @Transactional(readOnly = true)
    public ViewAllProcessDTOResponse viewAllProcess(ViewAllProcessDTORequest request, HttpSession session) {

        validateAllRequest(request, session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() != 0)  ? request.getPageIndex() : 1;

        ArrayList<String> requestTypeIdList = new ArrayList<>();

        String role = (String) session.getAttribute("ROLE");

        if (role == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

// MANAGER: xem tất cả request
        if (Objects.equals(role, Roles.MANAGER.getValue())) {

            requestTypeIdList.addAll(List.of(
                    RequestType.ALLOCATION.getValue(),
                    RequestType.RETRIEVAL.getValue(),
                    RequestType.PROCUREMENT.getValue(),
                    RequestType.MAINTENANCE.getValue(),
                    RequestType.LIQUIDATION.getValue()
            ));

        }
// WAREHOUSE
        else if (Objects.equals(role, Roles.WAREHOUSE.getValue())) {

            requestTypeIdList.addAll(List.of(
                    RequestType.PROCUREMENT.getValue(),
                    RequestType.RETRIEVAL.getValue(),
                    RequestType.ALLOCATION.getValue()
            ));

        }
// PURCHASING
        else if (Objects.equals(role, Roles.PURCHASING.getValue())) {

            requestTypeIdList.addAll(List.of(
                    RequestType.MAINTENANCE.getValue(),
                    RequestType.LIQUIDATION.getValue(),
                    RequestType.PROCUREMENT.getValue()
            ));

        }
// DEPARTMENT_MANAGER
        else if (Objects.equals(role, Roles.DEPARTMENT_MANAGER.getValue())) {

            requestTypeIdList.add(RequestType.ALLOCATION.getValue());

        }
// ROLE KHÁC → CẤM
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }


        // Get data from database
        List<RequestProcessAllServiceResponse> allProcessResponses = assetAllProcessService.viewAllProcess(
                AllProcessServiceRequest.builder()
                        .requestStatusId(request.getRequestStatusId())
                        .requestTypeId(request.getRequestTypeId())
                 //       .approvalStatusId(request.getApprovalStatusId())
                        .requestTypeIdList(requestTypeIdList)
                        .offset((pageIndex-1)*PAGE_SIZE)
                        .pageSize(PAGE_SIZE)
                        .build());

        if (allProcessResponses.isEmpty()) {
            return ViewAllProcessDTOResponse.builder()    // ViewAllProcessDTOResponse có 2 phương thức
                    .allProcessResponses(Collections.emptyList())
                    .filters(FilterAllDTOResponse.builder()
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

        return ViewAllProcessDTOResponse.builder()
                .allProcessResponses(
                        allProcessResponses.stream().map(
                                        entity -> {

                                            // --- Logic tính toán chèn vào ---
                                            // (Kiểm tra xem requestType có phải là Internal không)
                                            boolean isInternal = Objects.equals(entity.requestTypeId, RequestType.ALLOCATION.getValue())
                                                    || Objects.equals(entity.requestTypeId, RequestType.RETRIEVAL.getValue());

                                            // --- 2. Bắt buộc phải có từ khóa RETURN ở đây ---
                                            return AllProcessDTOResponse.builder()
                                                    .id(entity.requestId)    //lấy mã định danh từng phiếu để detail từng request

                                                    .isInternal(isInternal) //  đánh dấu xem internal hay external(boolean)

                                                    .requestTypeName(RequestType.of(entity.requestTypeId).getName())
                                                    .requestedBy(entity.requestedBy)
                                                    .requestedDate(entity.requestedDate)
                                                    .requestStatusName(RequestStatus.of(entity.requestStatusId).getName())
                                                    .approvalBy(entity.approvalBy != null ? entity.approvalBy : null)
                                                    .approvalDate(entity.approvalDate)
                                                    .handoverDate(entity.handoverDate)
                                                    .note(entity.note)
                                                    .createdAt(entity.createdAt)
                                                    .build();

                                        }).toList()
                )
                .filters(FilterAllDTOResponse.builder()
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


    private void validateAllRequest(ViewAllProcessDTORequest request, HttpSession session) {

         //Check role
        if (Objects.equals(session.getAttribute("ROLE"), Roles.ADMIN.getValue())
//        || Objects.equals(session.getAttribute("ROLE"), Roles.DEPARTMENT_MANAGER.getValue())
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

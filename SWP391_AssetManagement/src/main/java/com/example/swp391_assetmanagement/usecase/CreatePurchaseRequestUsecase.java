package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDTORequest;
import com.example.swp391_assetmanagement.dto.request.CreatePurchaseRequestDetailDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.ExternalStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreatePurchaseRequestUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;
    private final UserService userService;

    @Transactional
    public void execute(CreatePurchaseRequestDTORequest request, HttpSession session) {

        Long userId = userService.getIdByUserCode(session.getAttribute("USER_CODE").toString());
        String role = (String) session.getAttribute("ROLE");

        if (!com.example.swp391_assetmanagement.enums.Roles.MANAGER.getValue().equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only manager can create purchase request");
        }

        // Check type request:  kiểm tra xem bản ghi đó có thực sự là đơn hàng Mua sắm
        if (!ObjectUtils.isEmpty(request.getAssetRequestId())) {
            String assetRequestType = assetRequestService.findRequestTypeById(request.getAssetRequestId());

            if (ObjectUtils.isEmpty(assetRequestType)
                    || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.PROCUREMENT.getValue())) {
                throw new ValidationException();
            }
        }
        // Check update or insert
        // insert
        if (Objects.isNull(request.getAssetRequestId())) {

            AssetRequest assetRequest = new AssetRequest();

            assetRequest.setRequestTypeId(RequestType.PROCUREMENT.getValue());
            assetRequest.setRequestedBy(userId);
            assetRequest.setRequestedDate(LocalDate.now());

            String statusId;
            if (request.getIsSubmitted()) {
                statusId = RequestStatus.APPROVED.getValue(); // Tự động duyệt nếu MANAGER nhấn gửi
                assetRequest.setApprovedBy(userId);
                assetRequest.setApprovedDate(LocalDate.now());
            } else {
                statusId = RequestStatus.DRAFT.getValue();
            }
            assetRequest.setRequestStatusId(statusId);
            // Insert to AssetRequest
            Long assetRequestId =
                    assetRequestService.createPurchaseRequestForm(assetRequest);

            // Insert to assetExternalRequestDetail
            List<AssetExternalRequestDetail> details =
                    request.getCreatePurchaseRequestDetailDTORequestList()
                            .stream()
                            .map(dto -> {

                                AssetExternalRequestDetail detail = new AssetExternalRequestDetail();

                                detail.setAssetRequestId(assetRequestId);
                                detail.setAssetTypeId(AssetType.of(dto.getAssetTypeId()).getValue());
                                detail.setExternalStatusId(request.getIsSubmitted()
                                        ? ExternalStatus.IN_PROGRESS.getValue() : ExternalStatus.DRAFT.getValue());
                                detail.setQuantity(dto.getQuantity());
                                detail.setNote(dto.getNote());

                                return detail;
                            })
                            .toList();
            assetExternalRequestDetailService.batchInsert(details);

        } else {

            Integer countRequest = assetRequestService.countById(request.getAssetRequestId(), RequestStatus.PENDING_APPROVAL.getValue());

            if (countRequest > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is invalid!");
            }

            // Get list assetExternalRequestDetail from DB
            // lấy dữ liệu cũ trong DB ra để so sánh với dữ liệu mới gửi lên
            List<AssetExternalRequestDetail> dbDetails =
                    assetExternalRequestDetailService.getByAssetRequestIdForUpdate(request.getAssetRequestId());

            Set<Long> dbIds = dbDetails.stream()
                    .map(detail -> detail.id)
                    .collect(Collectors.toSet());

            List<CreatePurchaseRequestDetailDTORequest> updateDTOs = Collections.emptyList(); // những món đã có ID - cần sửa
            List<CreatePurchaseRequestDetailDTORequest> insertDTOs = Collections.emptyList(); // những món chưa có ID - cần thêm mới

            if (!CollectionUtils.isEmpty(request.getCreatePurchaseRequestDetailDTORequestList())) {
                Map<Boolean, List<CreatePurchaseRequestDetailDTORequest>> partitioned =
                        request.getCreatePurchaseRequestDetailDTORequestList().stream()
                                .peek(item -> {
                                    if (item.getAssetExternalRequestDetailId() != null
                                            && !dbIds.contains(item.getAssetExternalRequestDetailId())) {
                                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is invalid!");
                                    }
                                })
                                .collect(Collectors.partitioningBy(item -> item.getAssetExternalRequestDetailId() != null));

                updateDTOs = partitioned.get(true);
                insertDTOs = partitioned.get(false);
            }

            Set<Long> requestUpdateIds = updateDTOs.stream()
                    .map(CreatePurchaseRequestDetailDTORequest::getAssetExternalRequestDetailId)
                    .collect(Collectors.toSet());

            // Delete record
            List<Long> idsToDelete = dbIds.stream()
                    .filter(id -> !requestUpdateIds.contains(id))
                    .toList();

            if (!idsToDelete.isEmpty()) {
                assetExternalRequestDetailService.batchDelete(idsToDelete);
            }

            // Insert to assetExternalRequestDetail if exist
            // Thêm các dòng mới mà người dùng vừa nhập thêm vào bảng.
            if (!insertDTOs.isEmpty()) {
                List<AssetExternalRequestDetail> toInsert = insertDTOs.stream()
                        .map(dto -> {
                            AssetExternalRequestDetail entity = new AssetExternalRequestDetail();
                            entity.setAssetRequestId(request.getAssetRequestId());
                            entity.setAssetTypeId(AssetType.of(dto.getAssetTypeId()).getValue());
                            entity.setExternalStatusId(request.getIsSubmitted() ? ExternalStatus.IN_PROGRESS.getValue() : ExternalStatus.DRAFT.getValue());
                            entity.setNote(dto.getNote());
                            entity.setQuantity(dto.getQuantity());
                            return entity;
                        }).toList();
                assetExternalRequestDetailService.batchInsert(toInsert);
            }

            // Update assetExternalRequestDetail if exist
            // Cập nhật thông tin (số lượng, ghi chú) cho các dòng cũ đã có sẵn.
            if (!updateDTOs.isEmpty()) {
                List<AssetExternalRequestDetail> toUpdate = updateDTOs.stream()
                        .map(dto -> {
                            AssetExternalRequestDetail entity = new AssetExternalRequestDetail();
                            entity.setId(dto.getAssetExternalRequestDetailId());
                            entity.setAssetRequestId(request.getAssetRequestId());
                            entity.setAssetTypeId(AssetType.of(dto.getAssetTypeId()).getValue());
                            entity.setExternalStatusId(request.getIsSubmitted() ? ExternalStatus.IN_PROGRESS.getValue() : ExternalStatus.DRAFT.getValue());
                            entity.setNote(dto.getNote());
                            entity.setQuantity(dto.getQuantity());
                            return entity;
                        }).toList();
                assetExternalRequestDetailService.batchUpdate(toUpdate);
            }

            // Update AssetRequest if status = submit
            assetRequestService.findAssetRequestByIdForUpdate(request.getAssetRequestId()).ifPresent(
                    assetRequest -> {
                        if (request.getIsSubmitted()) {
                            assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue());
                            assetRequest.setApprovedBy(userId);
                            assetRequest.setApprovedDate(LocalDate.now());
                        } else {
                            assetRequest.setRequestStatusId(RequestStatus.DRAFT.getValue());
                        }
                        assetRequestService.updatePurchaseRequest(assetRequest);
                    }
            );
        }
    }
}

package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.ViewAssetByUserDisabledDTORequest;
import com.example.swp391_assetmanagement.dto.response.RecoverItemDetailDTO;
import com.example.swp391_assetmanagement.dto.response.RecoverProcessDTOResponse;
import com.example.swp391_assetmanagement.dto.response.ViewAssetByUserDisabledDTOResponse;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.enums.*;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.CreateRequestRecoverService;
import com.example.swp391_assetmanagement.service.ViewAssetByUserDisabledService;
import com.example.swp391_assetmanagement.service.servicerequest.RecoverServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.ViewAssetByUserDisabledServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.ViewAssetByUserDisabledServiceResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseRecoverUsecase {

    private final AssetRequestService assetRequestService;
    private final CreateRequestRecoverService recoverService;
    private final Integer PAGE_SIZE = 15;
    private final ViewAssetByUserDisabledService service;

    //get request to show in request detail
    public AssetRequest getRequestInfo(Long requestId) {
        return assetRequestService.findById(requestId);
    }
    //get request detail by request id
    public List<AssetInternalRequestDetail> getRequestDetails(Long requestId) {
        return assetRequestService.findDetailsByRequestId(requestId);
    }
//    //set request status approve to in progress
//    @Transactional
//    public void prepareProcessing(Long requestId) {
//        String currentStatus = assetRequestService.getRequestStatusById(requestId);
//        if ("03".equals(currentStatus)) { // APPROVED
//            assetRequestService.updateRequestStatus(requestId, "05"); // IN_PROGRESS
//        }
//    }
    @Transactional(readOnly = true)
    public RecoverProcessDTOResponse getRecoverProcessData(Long requestId) {
        // 1. Lấy dữ liệu từ Service
        AssetRequest request = assetRequestService.findById(requestId);
        if (request == null) return null;

        List<AssetInternalRequestDetail> details = assetRequestService.findDetailsByRequestId(requestId);

        // 2. Mapping sang DTO (Giữ nguyên các trường bạn đã định nghĩa)
        return RecoverProcessDTOResponse.builder()
                .requestId(request.id)
                .requestStatusId(request.requestStatusId)
                // Bạn có thể xử lý hiển thị tên Status ở đây nếu cần
                .requestedBy(String.valueOf(request.requestedBy))
                .requestedDate(request.requestedDate != null ? request.requestedDate.toString() : "")
                .note(request.note != null ? request.note : "N/A")
                .items(details.stream().map(d -> RecoverItemDetailDTO.builder()
                        .detailId(d.id)
                        .assetId(d.assetId)
                        .isDone(d.isDone)
                        // Ở đây tạm thời để ID, nếu muốn hiện Name bạn cần Service Join bảng
                        .fromUserName(String.valueOf(d.fromUserId))
                        .fromLocationName(d.fromLocationId)
                        .build()).toList())
                .build();
    }

    @Transactional
    public void prepareProcessing(Long requestId) {
        String currentStatus = assetRequestService.getRequestStatusById(requestId);
        if ("03".equals(currentStatus)) {
            assetRequestService.updateRequestStatus(requestId, "05");
        }
    }
    //set request retrival to warehouse and update status
    @Transactional
    public void executeRecovery(Long detailId, Long requestId) {
        RecoverServiceRequest serviceReq = RecoverServiceRequest.builder()
                .detailId(detailId)
                .targetStatus("08") // STOCKED
                .targetLocation("05")  // WAREHOUSE
                .build();

        assetRequestService.confirmDetailAndRestoreAsset(serviceReq);

        if (assetRequestService.isAllDetailsDone(requestId)) {
            assetRequestService.updateRequestStatus(requestId, "06"); // COMPLETED
            assetRequestService.updateHandoverDate(requestId, LocalDate.now());
        }
    }

    //View assets to retrival
    @Transactional(readOnly = true)
    public ViewAssetByUserDisabledDTOResponse viewAssetDisabled(ViewAssetByUserDisabledDTORequest request, HttpSession session) {
        // 1. Validate quyền truy cập (Admin/Manager mới được xem chẳng hạn)
        validateAccess(session);

        int pageIndex = (request.getPageIndex() != null && request.getPageIndex() > 0) ? request.getPageIndex() : 1;

        // 2. Map request sang ServiceRequest
        ViewAssetByUserDisabledServiceRequest serviceRequest = ViewAssetByUserDisabledServiceRequest.builder()
                .userStatus("03") // Trạng thái Disable
                .assetStatusId("02") //Trạng thái đang sử dụng
                .assetCode(request.getAssetCode())
                .locationId(request.getLocationId())
                .assetTypeId(request.getAssetTypeId())
                .offset((pageIndex - 1) * PAGE_SIZE)
                .pageSize(PAGE_SIZE)
                .build();

        List<ViewAssetByUserDisabledServiceResponse> serviceResponses = service.selectAllAssetByUserDisable(serviceRequest);

        if (serviceResponses.isEmpty()) {
            return ViewAssetByUserDisabledDTOResponse.builder()
                    .assets(Collections.emptyList())
                    .filters(ViewAssetByUserDisabledDTOResponse.FilterUserDTOResponse.builder() // ĐỪNG QUÊN DÒNG NÀY
                            .assetCode(request.getAssetCode())
                            .locationId(request.getLocationId())
                            .assetTypeId(request.getAssetTypeId())
                            .build())
                    .page(pageIndex).pageSize(PAGE_SIZE).totalAsset(0).totalPages(0)
                    .build();
        }

        // 3. Tính toán phân trang
        int totalAssets = serviceResponses.get(0).getTotalItems();
        int totalPages = (int) Math.ceil((double) totalAssets / PAGE_SIZE);

        // 4. Map Response (Lưu ý: Bạn nên tạo một List bên trong ViewAssetByUserDisabledDTOResponse)
        // Trong hàm viewAssetDisabled của ViewAssetByUserDisabledUsecase
        return ViewAssetByUserDisabledDTOResponse.builder()
                .assets(serviceResponses.stream()
                        .map(this::mapToItemResponse)
                        .toList())
                // THÊM ĐOẠN NÀY ĐỂ GIỮ FILTER
                .filters(ViewAssetByUserDisabledDTOResponse.FilterUserDTOResponse.builder()
                        .assetCode(request.getAssetCode())
                        .locationId(request.getLocationId())
                        .assetTypeId(request.getAssetTypeId())
                        .build())
                .page(pageIndex)
                .pageSize(PAGE_SIZE)
                .totalAsset(totalAssets)
                .totalPages(totalPages)
                .hasNextPage(pageIndex < totalPages)
                .hasPreviousPage(pageIndex > 1)
                .build();
    }
    //block user
    private void validateAccess(HttpSession session) {
        String role = (String) session.getAttribute("ROLE");
        // Nếu là Client hoặc các role thấp thì chặn
        if (List.of("CLIENT", "WAREHOUSE").contains(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền!");
        }
    }
    //map to set in view
    private ViewAssetByUserDisabledDTOResponse.AssetDetailResponse mapToItemResponse(ViewAssetByUserDisabledServiceResponse entity) {
        return ViewAssetByUserDisabledDTOResponse.AssetDetailResponse.builder()
                .assetCode(entity.getAssetCode())
                .description(entity.getDescription())
                .receivedDate(entity.getReceivedDate())
                .locationName(Location.of(entity.getLocationId()).getName())
                .assetTypeName(AssetType.of(entity.getAssetTypeId()).getName())
                .assetStatusName(AssetStatus.of(entity.getAssetStatusId()).getName())
                .username(entity.getUsername())
                .userStatus(entity.getUserStatus())
                .build();
    }

    //Create request and request internal detail
    @Transactional
    public void execute(List<String> assetCodes, String userCode) {
        // 1. Lấy ID Manager từ Code
        Long userId = recoverService.getUserIdByCode(userCode);

        // 2. SELECT danh sách tài sản dựa trên mã Code người dùng chọn từ UI
        List<Assets> selectedAssets = recoverService.getAssetsByCodes(assetCodes);

        // 3. Tạo Request tổng
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setRequestTypeId(RequestType.RETRIEVAL.getValue());
        assetRequest.setRequestedBy(userId);
        assetRequest.setRequestedDate(LocalDate.now());
        assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue());
        assetRequest.setApprovedBy(userId);
        assetRequest.setApprovedDate(LocalDate.now());

        // Lưu Request và lấy ID sinh ra
        Long assetRequestId = recoverService.insertRecover(assetRequest);

        // 4. Lưu chi tiết và gom danh sách ID tài sản để Update trạng thái
        List<Long> assetIdsToUpdate = selectedAssets.stream()
                .map(asset -> {
                    // Tạo detail
                    AssetInternalRequestDetail detail = new AssetInternalRequestDetail();
                    detail.setAssetRequestId(assetRequestId);
                    detail.setAssetId(asset.id);
                    detail.setAssetTypeId(asset.assetTypeId);
                    detail.setQuantity(1);
                    detail.setFromLocationId(asset.locationId);
                    detail.setToLocationId("05");
                    detail.setFromUserId(asset.currentUserId);
                    detail.setIsDone(false);

                    // Lưu từng detail bằng hàm của bạn
                    recoverService.createRequestInternalRecover(detail);

                    return asset.id; // Trả về ID để gom vào list update
                })
                .collect(Collectors.toList());

        // 5. UPDATE trạng thái tất cả tài sản đã chọn sang '09' (RETRIEVAL)
        // Truyền list ID động, không hề hardcode
        recoverService.updateAssetsToRetrieval(assetIdsToUpdate);
    }
}
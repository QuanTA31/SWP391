package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.entity.Location;
import com.example.swp391_assetmanagement.service.*;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryConfirmServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryCreateServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryItemServiceResponse;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryProcessServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryUsecase {
    private final InventoryService inventoryService;
    private final AssetRequestService assetRequestService;
    private final AssetService assetService;
    private final LocationService locationService;
    private final UserService userService;

    // Lấy dữ liệu cho màn hình GET (Nếu cần)
    public List<com.example.swp391_assetmanagement.enums.Location> prepareData() {
        List<Location> locations = locationService.selectLocationsWithAssets();
        return locations.stream()
                .map(location -> com.example.swp391_assetmanagement.enums.Location.of(location.id))
                .toList();
    }

    @Transactional
    public void executeCreate(InventoryCreateServiceRequest request) {
        // 1. Tạo Header bằng SQL tự viết (insertInventoryRequest)
        Long managerId = userService.getIdByUserCode(request.getUserCode());

        AssetRequest entity = new AssetRequest();
        entity.setRequestTypeId("06"); // INVENTORY
        entity.setRequestStatusId("11"); // WAITING
        entity.setRequestedBy(managerId);
        entity.setRequestedDate(LocalDate.now());
        entity.setApprovedBy(managerId);
        entity.setApprovedDate(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());

        Long newRequestId = inventoryService.insertRequest(entity);

        // 2. Lấy Assets tại phòng ban và Snapshot vào Detail
        List<Assets> assets = assetService.findByLocationAndStatus(request.getLocationId(), "02");
        for (Assets a : assets) {
            AssetInternalRequestDetail detail = new AssetInternalRequestDetail();
            detail.setAssetRequestId(newRequestId);
            detail.setAssetId(a.id);
            detail.setFromUserId(a.currentUserId);
            detail.setFromLocationId(a.locationId);
            detail.setAssetTypeId(a.assetTypeId);
            detail.setIsDone(false);
            detail.setCreatedAt(LocalDateTime.now());

            inventoryService.insertDetail(detail);
        }
    }

    @Transactional
    public InventoryProcessServiceResponse executeProcess(InventoryProcessServiceRequest request) {
        // 1. Lấy thông tin Header
        AssetRequest header = assetRequestService.findById(request.getRequestId());

        // 2. Nếu đang ở trạng thái WAITING (11), chuyển sang IN_PROGRESS (05)
        if ("11".equals(header.requestStatusId)) {
            header.setRequestStatusId("05");
            inventoryService.updateRequest(header);
        }

        // 3. Lấy danh sách tài sản kèm Full Name qua SQL JOIN (selectInventoryItems)
        List<InventoryItemServiceResponse> items = inventoryService.selectItems(
                request.getRequestId(),
                request.getAssetTypeId(),
                request.getFullName()
        );

        return InventoryProcessServiceResponse.builder()
                .header(header)
                .items(items)
                .build();
    }

    @Transactional
    public void executeConfirm(InventoryConfirmServiceRequest request) {
        // 1. Tìm thông tin dòng detail
        AssetInternalRequestDetail detail = assetRequestService.findDetailById(request.getDetailId());

        // 2. Cập nhật trạng thái tài sản sang LOST (07) nếu Warehouse chọn
        if ("07".equals(request.getSelectedStatus())) {
            Assets asset = assetService.findById(detail.assetId);
            asset.setAssetStatusId("07");
            inventoryService.updateAssetStatus(asset);
        }

        // 3. Đánh dấu đã kiểm kê xong cho dòng này (updateInventoryDetail)
        detail.setIsDone(true);
        detail.setNote("Confirmed at " + LocalDateTime.now());
        inventoryService.updateDetail(detail);

        // 4. Kiểm tra hoàn thành (countUnfinishedInventoryItems)
        int unfinishedCount = inventoryService.countUnfinishedItems(detail.assetRequestId);
        if (unfinishedCount == 0) {
            AssetRequest header = assetRequestService.findById(detail.assetRequestId);
            header.setRequestStatusId("06"); // COMPLETED
            header.handoverDate = LocalDate.now();
            inventoryService.updateRequest(header);
        }
    }
}

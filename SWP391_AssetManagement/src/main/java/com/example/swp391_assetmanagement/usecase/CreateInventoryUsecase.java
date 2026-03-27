package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.InventoryCreateDTORequest;
import com.example.swp391_assetmanagement.entity.AssetInternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.Assets;
import com.example.swp391_assetmanagement.entity.Location;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.InventoryService;
import com.example.swp391_assetmanagement.service.LocationService;
import com.example.swp391_assetmanagement.service.UserService;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryCreateServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateInventoryUsecase {
    private final InventoryService inventoryService;
    private final AssetService assetService;
    private final LocationService locationService;
    private final UserService userService;

    public List<com.example.swp391_assetmanagement.enums.Location> prepareData() {
        List<Location> locations = locationService.selectLocationsWithAssets();
        return locations.stream()
                .map(location -> com.example.swp391_assetmanagement.enums.Location.of(location.id))
                .toList();
    }

    @Transactional
    public void execute(InventoryCreateDTORequest dtoRequest, String userCode) {
        InventoryCreateServiceRequest serviceRequest = InventoryCreateServiceRequest.builder()
                .locationId(dtoRequest.getLocationId())
                .userCode(userCode)
                .build();

        Long managerId = userService.getIdByUserCode(serviceRequest.getUserCode());

        AssetRequest entity = new AssetRequest();
        entity.setRequestTypeId("06"); // INVENTORY
        entity.setRequestStatusId("03"); // APPROVED
        entity.setRequestedBy(managerId);
        entity.setRequestedDate(LocalDate.now());
        entity.setApprovedBy(managerId);
        entity.setApprovedDate(LocalDate.now());
        entity.setCreatedAt(LocalDateTime.now());

        Long newRequestId = inventoryService.insertRequest(entity);

        List<Assets> assets;
        if ("05".equals(serviceRequest.getLocationId())) {
            // WAREHOUSE: include ALL assets in this location
            assets = inventoryService.findByLocation(serviceRequest.getLocationId());
        } else {
            // OTHER: only include ASSIGNED assets
            assets = assetService.findByLocationAndStatus(serviceRequest.getLocationId(), "02");
        }

        for (Assets a : assets) {
            AssetInternalRequestDetail detail = new AssetInternalRequestDetail();
            detail.setAssetRequestId(newRequestId);
            detail.setAssetId(a.id);
            detail.setFromUserId(a.currentUserId);
            detail.setFromLocationId(a.locationId);
            detail.setAssetTypeId(a.assetTypeId);
            detail.setIsDone(false); // DEFAULT TO FALSE (Lost) AS REQUESTED
            detail.setCreatedAt(LocalDateTime.now());

            inventoryService.insertDetail(detail);
        }
    }
}

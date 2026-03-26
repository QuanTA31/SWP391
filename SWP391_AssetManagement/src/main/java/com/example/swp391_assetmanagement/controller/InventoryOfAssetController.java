package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.common.RoleChecker;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Roles;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import com.example.swp391_assetmanagement.dto.request.InventoryConfirmDTORequest;
import com.example.swp391_assetmanagement.dto.request.InventoryCreateDTORequest;
import com.example.swp391_assetmanagement.dto.request.InventoryProcessDTORequest;
import com.example.swp391_assetmanagement.dto.response.InventoryItemDTOResponse;
import com.example.swp391_assetmanagement.dto.response.InventoryProcessDTOResponse;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryConfirmServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryCreateServiceRequest;
import com.example.swp391_assetmanagement.service.servicerequest.InventoryProcessServiceRequest;
import com.example.swp391_assetmanagement.service.serviceresponse.InventoryProcessServiceResponse;
import com.example.swp391_assetmanagement.usecase.InventoryUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class InventoryOfAssetController {

    private final InventoryUsecase inventoryUsecase;
    private final RoleChecker roleChecker;

    @GetMapping("/inventory/create")
    public String showCreateForm(Model model, HttpSession session) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        // Lấy danh sách Location (từ Enum hoặc DB) thông qua Usecase
        // Ở đây mình giả định Usecase đã có method prepareData() trả về List<Location>
        model.addAttribute("locations", inventoryUsecase.prepareData());

        // Trả về file HTML mà mình đã viết cho bạn ở trên
        return "CreateInventoryRequest";
    }

    @PostMapping("/inventory/create")
    public String createInventory(@ModelAttribute InventoryCreateDTORequest dtoRequest, HttpSession session) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        String userCode = (String) session.getAttribute("USER_CODE");


        // Mapping DTO sang ServiceRequest
        InventoryCreateServiceRequest serviceRequest = InventoryCreateServiceRequest.builder()
                .locationId(dtoRequest.getLocationId())
                .userCode(userCode)
                .build();

        inventoryUsecase.executeCreate(serviceRequest);
        return "redirect:/viewRequest";
    }

    @GetMapping("/inventory/process")
    public String processInventory(@ModelAttribute InventoryProcessDTORequest dtoRequest, Model model, HttpSession session) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER,Roles.WAREHOUSE);

        // Mapping DTO sang ServiceRequest
        InventoryProcessServiceRequest serviceRequest = InventoryProcessServiceRequest.builder()
                .requestId(dtoRequest.getRequestId())
                .assetTypeId(dtoRequest.getAssetTypeId())
                .fullName(dtoRequest.getFullName())
                .build();

        InventoryProcessServiceResponse serviceResponse = inventoryUsecase.executeProcess(serviceRequest);

        // Chuyển đổi ServiceResponse sang DTOResponse để hiển thị
        InventoryProcessDTOResponse dtoResponse = InventoryProcessDTOResponse.builder()
                .requestId(serviceResponse.getHeader().id)
                .statusName(serviceResponse.getHeader().requestStatusId)
                .items(serviceResponse.getItems().stream().map(item ->
                        InventoryItemDTOResponse.builder()
                                .detailId(item.getDetailId())
                                .assetCode(item.getAssetCode())
                                .userFullName(item.getUserFullName())
                                .assetTypeName(AssetType.of(item.getAssetTypeId()).getName())
                                .isDone(item.getIsDone())
                                .statusId(item.getStatusId())
                                .build()
                ).toList())
                .build();

        model.addAttribute("data", dtoResponse);
        return "InventoryDetailView";
    }

    @PostMapping("/api/inventory/confirm")
    @ResponseBody
    public ResponseEntity<?> confirm(@RequestBody InventoryConfirmDTORequest dtoRequest, HttpSession session) {

        Object userCode = session.getAttribute("USER_CODE");
        if (userCode == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired. Please login again.");
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER,Roles.WAREHOUSE);

        // Mapping DTO sang ServiceRequest
        InventoryConfirmServiceRequest serviceRequest = InventoryConfirmServiceRequest.builder()
                .detailId(dtoRequest.getDetailId())
                .selectedStatus(dtoRequest.getSelectedStatus())
                .build();

        inventoryUsecase.executeConfirm(serviceRequest);
        return ResponseEntity.ok().build();
    }
}

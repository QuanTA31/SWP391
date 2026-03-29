package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.common.RoleChecker;
import com.example.swp391_assetmanagement.enums.Roles;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import com.example.swp391_assetmanagement.dto.request.InventoryConfirmDTORequest;
import com.example.swp391_assetmanagement.dto.request.InventoryCreateDTORequest;
import com.example.swp391_assetmanagement.dto.request.InventoryProcessDTORequest;
import com.example.swp391_assetmanagement.dto.response.InventoryProcessDTOResponse;
import com.example.swp391_assetmanagement.usecase.CreateInventoryUsecase;
import com.example.swp391_assetmanagement.usecase.ViewInventoryDetailUsecase;
import com.example.swp391_assetmanagement.usecase.ConfirmInventoryItemUsecase;
import com.example.swp391_assetmanagement.usecase.CompleteInventoryUsecase;
import com.example.swp391_assetmanagement.usecase.UpdateAssetStatusInventoryUsecase;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class InventoryOfAssetController {

    private final CreateInventoryUsecase createInventoryUsecase;
    private final ViewInventoryDetailUsecase viewInventoryDetailUsecase;
    private final ConfirmInventoryItemUsecase confirmInventoryItemUsecase;
    private final CompleteInventoryUsecase completeInventoryUsecase;
    private final UpdateAssetStatusInventoryUsecase updateAssetStatusInventoryUsecase;
    private final RoleChecker roleChecker;

    // view Create request
    @GetMapping("/inventory/create")
    public String showCreateForm(Model model, HttpSession session) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        // Lấy danh sách Location (từ Enum hoặc DB) thông qua Usecase
        model.addAttribute("locations", createInventoryUsecase.prepareData());

        return "CreateInventoryRequest";
    }

    // Create request
    @PostMapping("/inventory/create")
    public String createInventory(@ModelAttribute InventoryCreateDTORequest dtoRequest, HttpSession session) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);
        String userCode = (String) session.getAttribute("USER_CODE");

        createInventoryUsecase.execute(dtoRequest, userCode);
        return "redirect:/viewRequest";
    }

    // view process
    @GetMapping("/inventory/process")
    public String processInventory(@ModelAttribute InventoryProcessDTORequest dtoRequest, Model model, HttpSession session) {

        if (session.getAttribute("USER_CODE") == null) {
            return "redirect:/login";
        }

        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER,Roles.WAREHOUSE);

        InventoryProcessDTOResponse dtoResponse = viewInventoryDetailUsecase.execute(dtoRequest);

        model.addAttribute("data", dtoResponse);
        return "InventoryDetailView";
    }

//    // Warehouse confirm individual assets
//    @PostMapping("/api/inventory/confirm")
//    @ResponseBody
//    public ResponseEntity<?> confirm(@RequestBody InventoryConfirmDTORequest dtoRequest, HttpSession session) {
//
//        Object userCode = session.getAttribute("USER_CODE");
//        if (userCode == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired. Please login again.");
//        }
//
//        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER,Roles.WAREHOUSE);
//
//        confirmInventoryItemUsecase.execute(dtoRequest);
//        return ResponseEntity.ok().build();
//    }

    // Warehouse confirm done inventory
    @PostMapping("/api/inventory/complete")
    @ResponseBody
    public ResponseEntity<?> completeInventory(@RequestBody com.example.swp391_assetmanagement.dto.request.InventoryCompleteDTORequest payload, HttpSession session) {
        Object userCode = session.getAttribute("USER_CODE");
        if (userCode == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired.");
        }
        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER,Roles.WAREHOUSE);

        completeInventoryUsecase.execute(payload);
        return ResponseEntity.ok().build();
    }

    // Manager confirm and click butotn update status assets
    @PostMapping("/api/inventory/update-assets")
    @ResponseBody
    public ResponseEntity<?> updateAssets(@RequestBody java.util.Map<String, Long> payload, HttpSession session) {
        Object userCode = session.getAttribute("USER_CODE");
        if (userCode == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Session expired.");
        }
        roleChecker.requireRole(session.getAttribute("USER_CODE").toString(), Roles.MANAGER);

        Long requestId = payload.get("requestId");
        updateAssetStatusInventoryUsecase.execute(requestId);
        return ResponseEntity.ok().build();
    }
}

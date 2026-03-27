package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.serviceresponse.PurchaseOrderItemResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetPurchaseOrderUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public void execute(Long assetRequestId, HttpSession session, Model model) {

        // Role check
        String role = (String) session.getAttribute("ROLE");
        if (!Roles.MANAGER.getValue().equals(role)
                && !Roles.PURCHASING.getValue().equals(role)
                && !Roles.WAREHOUSE.getValue().equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không có quyền xem Purchase Order"
            );
        }

        // Load approved option-detail items
        List<PurchaseOrderItemResponse> items =
                assetExternalRequestDetailService.findPurchaseOrderItems(assetRequestId);

        // Enrich: map assetTypeName & compute totalPrice
        BigDecimal grandTotal = BigDecimal.ZERO;
        for (PurchaseOrderItemResponse item : items) {
            // Map asset type name
            try {
                item.assetTypeName = AssetType.of(item.assetTypeId).getName();
            } catch (Exception e) {
                item.assetTypeName = item.assetTypeId;
            }

            // Compute total price
            if (item.unitPrice != null && item.quantity != null) {
                item.totalPrice = item.unitPrice.multiply(BigDecimal.valueOf(item.quantity));
                grandTotal = grandTotal.add(item.totalPrice);
            } else {
                item.totalPrice = BigDecimal.ZERO;
            }
        }

        // Load request info
        var assetRequest = assetRequestService.findByUpdate(assetRequestId);

        model.addAttribute("purchaseOrderItems", items);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("assetRequestId", assetRequestId);
        model.addAttribute("assetRequest", assetRequest);
        model.addAttribute("role", role);
    }
}

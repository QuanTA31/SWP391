package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.OptionDetailFormDTORequest;
import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EditPurchaseOptionUsecase {

    private final OptionDetailService optionDetailService;
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public void execute(
            Long requestDetailId,
            OptionDetailFormDTORequest form,
            HttpSession session
    ) {
        AssetExternalRequestDetail detail = assetExternalRequestDetailService.findToUpdate(requestDetailId);
        Long requestId = detail.getAssetRequestId();

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(requestId);

        if ((ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.PROCUREMENT.getValue()))) {
            throw new ValidationException("Invalid request type");
        }

        //check role
        String role = (String) session.getAttribute("ROLE");

        if (!Roles.MANAGER.getValue().equals(role)
                && !Roles.PURCHASING.getValue().equals(role)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này"
            );
        }

        validate(form);

        OptionDetail option = optionDetailService.getById(form.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        option.setMerchant(form.getMerchant());
        option.setDescription(form.getDescription());
        option.setUnitPrice(form.getUnitPrice());
        option.setWarrantyPeriod(form.getWarrantyPeriod());

        optionDetailService.edit(option);
    }

    private void validate(OptionDetailFormDTORequest form) {
        if (form == null) {
            throw new IllegalArgumentException("Form must not be null");
        }

        if (form.getMerchant() == null || form.getMerchant().trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant is required");
        }

        if (form.getDescription() == null || form.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (form.getUnitPrice() == null) {
            throw new IllegalArgumentException("Unit price is required");
        }

        if (form.getUnitPrice().signum() <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than 0");
        }

        if (form.getWarrantyPeriod() != null
                && form.getWarrantyPeriod().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Warranty period must be in the future");
        }
    }
}
package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dto.request.OptionDetailFormRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOptionDetailUseCase {

    private final OptionDetailService optionDetailService;
    private final AuthGuardService authGuardService;

    public void execute(
            Long requestDetailId,
            OptionDetailFormRequest form
    ) {
        authGuardService.checkAuthenticated();
        authGuardService.checkCanAccessRequest(requestDetailId);

        validate(form);

        OptionDetail option = new OptionDetail();
        option.setAssetExternalRequestDetailId(requestDetailId);
        option.setMerchant(form.getMerchant());
        option.setDescription(form.getDescription());
        option.setUnitPrice(form.getUnitPrice());
        option.setWarrantyPeriod(form.getWarrantyPeriod());
        option.setIsSelected(false);
        option.setApprovedDate(null);
        option.setApproverBy(null);

        optionDetailService.create(option);
    }

    private void validate(OptionDetailFormRequest form) {
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


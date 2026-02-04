package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EditOptionDetailUseCase {

    private final OptionDetailService optionDetailService;
    private final AuthGuardService authGuardService;

    public void execute(
            Long requestDetailId,
            List<OptionDetail> plans
    ) {
        authGuardService.checkAuthenticated();
        authGuardService.checkCanAccessRequest(requestDetailId);

        for (OptionDetail row : plans) {

            if (row.id != null) {
                OptionDetail existing = optionDetailService
                        .getById(row.id)
                        .orElse(null);

                if (existing == null) {
                    continue;
                }

                existing.merchant = row.merchant;
                existing.description = row.description;
                existing.unitPrice = row.unitPrice;
                existing.warrantyPeriod = row.warrantyPeriod;

                optionDetailService.update(existing);

            } else {
                row.assetExternalRequestDetailId = requestDetailId;
                row.isSelected = false;
                row.approvedDate = null;
                row.approverBy = null;

                optionDetailService.create(row);
            }
        }
    }
}

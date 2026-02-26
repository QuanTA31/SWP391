package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ApproveOptionDetailUsecase {

    private final OptionDetailService optionDetailService;
    private final AuthGuardService authGuardService;

    public void execute(
            Long optionId,
            Long requestDetailId,
            boolean selected
    ) {
        authGuardService.checkManager();
        authGuardService.checkCanAccessRequest(requestDetailId);

        Long approverId = authGuardService.getCurrentUserId();

        OptionDetail plan = optionDetailService
                .getById(optionId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Option detail not found"
                        )
                );

        if (selected) {
            optionDetailService.unselectByRequestDetailId(requestDetailId);
            plan.approvedDate = LocalDate.now();
            plan.approverBy = approverId;
        } else {
            plan.approvedDate = null;
            plan.approverBy = null;
        }

        plan.isSelected = selected;
        optionDetailService.update(plan);
    }

}

package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.auth.AuthGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOptionDetailUseCase {

    private final OptionDetailService optionDetailService;
    private final AuthGuardService authGuardService;

    public void execute(
            Long requestDetailId,
            List<OptionDetail> plans
    ) {
        authGuardService.checkAuthenticated();
        authGuardService.checkCanAccessRequest(requestDetailId);

        for (OptionDetail p : plans) {
            p.assetExternalRequestDetailId = requestDetailId;
            p.isSelected = false;
            p.approvedDate = null;
            p.approverBy = null;
        }

        optionDetailService.saveAll(plans);
    }
}

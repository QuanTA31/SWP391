package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ApproveOptionDetailUsecase {

    private final OptionDetailService optionDetailService;

    public void execute(
            Long optionId,
            Long requestDetailId,
            boolean selected,
            HttpSession session
    ) {

        //check role
        String role = (String) session.getAttribute("ROLE");

        if (!Roles.MANAGER.getValue().equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only manager can approve option detail"
            );
        }

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
            // plan.approverBy = (Long) session.getAttribute("USER_ID");
        } else {
            plan.approvedDate = null;
            plan.approverBy = null;
        }

        plan.isSelected = selected;
        optionDetailService.update(plan);
    }
}
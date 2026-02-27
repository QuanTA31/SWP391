package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.dao.OptionDetailDao;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.UserService;
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
    private final UserService userService;
    private final OptionDetailDao optionDetailDao;

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
        String userCode = (String) session.getAttribute("USER_CODE");
        Long userId = userService.getIdByUserCode(userCode);
        if (selected) {
            optionDetailService.unselectByRequestDetailId(requestDetailId);
            plan.setIsSelected(true);
            plan.setApprovedDate(LocalDate.now());
            plan.setApproverBy(userId);
        } else {
            plan.setIsSelected(false);
            plan.setApprovedDate(null);
            plan.setApproverBy(null);
        }
        optionDetailService.update(plan);
    }
}
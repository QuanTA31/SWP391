package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApproveOptionDetailUsecase {

    private final OptionDetailService optionDetailService;
    private final UserService userService;
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(
            Long optionId,
            Long requestDetailId,
            boolean selected,
            HttpSession session
    ) {
        //Check role
        String role = (String) session.getAttribute("ROLE");

        if (!Roles.MANAGER.getValue().equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only manager can approve option detail"
            );
        }
        //Lấy option
        OptionDetail plan = optionDetailService
                .getById(optionId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Option detail not found"
                        )
                );
        Integer count = optionDetailService.countByIdAndStatus(requestDetailId, Boolean.TRUE);

        if (count > 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String userCode = (String) session.getAttribute("USER_CODE");
        Long userId = userService.getIdByUserCode(userCode);

        if (selected && Objects.nonNull(plan)) {

            //Lấy detail
            AssetExternalRequestDetail detail = assetExternalRequestDetailService.findToUpdate(requestDetailId);

            Long requestId = detail.getAssetRequestId();

            //Lấy asset_request
            AssetRequest assetRequest =
                    assetRequestService.findToUpdate(requestId);

            //Check status ở đây
            if (Objects.isNull(assetRequest)
                    || !Objects.equals(RequestStatus.RESEARCH_DONE.getValue(), assetRequest.requestStatusId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            //Update status
            assetRequest.setRequestStatusId(RequestStatus.RESEARCH_DONE.getValue());
            assetRequestService.updatePurchaseRequestStatus(assetRequest);

            plan.setIsSelected(true);
            plan.setApprovedDate(LocalDate.now());
            plan.setApproverBy(userId);
        }
        //Update option
        optionDetailService.update(plan);
    }
}
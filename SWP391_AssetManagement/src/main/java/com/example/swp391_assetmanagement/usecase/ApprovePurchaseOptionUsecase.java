package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.ExternalStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.enums.Roles;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ApprovePurchaseOptionUsecase {

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
        // Get requestId
        Long requestId = assetExternalRequestDetailService.findAssetRequest(requestDetailId);

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(requestId);

        if ((ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.PROCUREMENT.getValue()))) {
            throw new ValidationException("Invalid request type");
        }

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
                                "Option detail not found"));
        if (Objects.nonNull(plan.isSelected)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Integer count = optionDetailService.countByIdAndStatus(requestDetailId, Boolean.TRUE);

        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        String userCode = (String) session.getAttribute("USER_CODE");
        Long userId = userService.getIdByUserCode(userCode);

        if (selected && Objects.nonNull(plan)) {

            //AssetExternalRequestDetail detail = assetExternalRequestDetailService.findToUpdate(requestDetailId);
            //Long requestId = detail.getAssetRequestId();

            //Lấy asset_request
            AssetRequest assetRequest =
                    assetRequestService.findByUpdate(requestId);

            Integer countBySelected = optionDetailService.countByIdAndIsSelected(requestDetailId, assetRequest.id);

            //Check status nếu status kphai là research thì báo lỗi
            if (!Objects.equals(RequestStatus.RESEARCH.getValue(), assetRequest.requestStatusId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // Update external_status_id = 03
            assetExternalRequestDetailService.updateExternalStatusId(
                    requestDetailId,
                    ExternalStatus.DONE.getValue()
            );

            if (countBySelected == 0) {
                Boolean isValidRequest = optionDetailService.checkValidRequest(requestDetailId, assetRequest.id);
                assetRequest.setRequestStatusId(isValidRequest
                        ? RequestStatus.RESEARCH_DONE.getValue() : RequestStatus.APPROVED.getValue());
                assetRequestService.updatePurchaseRequestStatus(assetRequest);
            }



            optionDetailService.resetAllByRequestDetailId(requestDetailId, userId);

            plan.setIsSelected(true);

        }
        //Update option
        optionDetailService.update(plan);
    }
}
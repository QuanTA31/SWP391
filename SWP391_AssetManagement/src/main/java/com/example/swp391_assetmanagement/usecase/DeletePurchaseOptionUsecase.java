package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
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
public class DeletePurchaseOptionUsecase {

    private final OptionDetailService optionDetailService;
    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    public void execute(Long requestDetailId,Long optionDetailId, HttpSession session) {

        AssetExternalRequestDetail detail = assetExternalRequestDetailService.findToUpdate(requestDetailId);
        Long requestId = detail.getAssetRequestId();

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(requestId);

        if ((ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.PROCUREMENT.getValue()))) {
            throw new ValidationException("Invalid request type");
        }

        String role = (String) session.getAttribute("ROLE");
        if (!Roles.MANAGER.getValue().equals(role)
                && !Roles.PURCHASING.getValue().equals(role)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bạn không có quyền truy cập chức năng này"
            );
        }
        optionDetailService.deleteById(optionDetailId);
    }
}
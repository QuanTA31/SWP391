package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateAssetRequestLiquidationUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(Long assetRequestId, HttpSession session) {

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(assetRequestId);

        if (ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.LIQUIDATION.getValue())) {
            throw new ValidationException();
        }

        Integer count = assetExternalRequestDetailService.countOptionDetail(assetRequestId);
        if(count > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status is invalid !");
        }

        AssetRequest assetRequest = assetRequestService.findByUpdate(assetRequestId);
        if (Objects.isNull(assetRequest)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status is invalid !");
        }
        assetRequest.setRequestStatusId(RequestStatus.RESEARCH.getValue());
        assetRequestService.updatePurchaseRequestStatus(assetRequest);
    }
}

package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
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
public class WarehouseCompleteUsecase {

    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(Long assetRequestId, HttpSession session) {

        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(assetRequestId);

        if (!(ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.PROCUREMENT.getValue()))) {
            throw new ValidationException();
        }

        Integer countRequest = assetRequestService.countById(assetRequestId, RequestStatus.STOCK_IN.getValue());
        if (countRequest == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is invalid!");
        }

        // Update AssetRequest if status = submit
        assetRequestService.findAssetRequestByIdForUpdate(assetRequestId).ifPresent(
                assetRequest -> {
                    assetRequest.setRequestStatusId(RequestStatus.COMPLETED.getValue());
                    assetRequestService.updatePurchaseRequest(assetRequest);
                }
        );
    }
}

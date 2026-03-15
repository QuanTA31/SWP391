package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
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

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StockinToCompletedUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(
            Long requestDetailId
    ){
        //Lấy detail
        AssetExternalRequestDetail detail = assetExternalRequestDetailService.findToUpdate(requestDetailId);

        Long requestId = detail.getAssetRequestId();

        //Lấy asset_request
        AssetRequest assetRequest =
                assetRequestService.findByUpdate(requestId);

        //Check status ở đây
        if (Objects.isNull(assetRequest)
                || !Objects.equals(RequestStatus.STOCK_IN.getValue(), assetRequest.requestStatusId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //Update status
        assetRequest.setRequestStatusId(RequestStatus.COMPLETED.getValue());
        assetRequestService.updatePurchaseRequestStatus(assetRequest);

    }
}


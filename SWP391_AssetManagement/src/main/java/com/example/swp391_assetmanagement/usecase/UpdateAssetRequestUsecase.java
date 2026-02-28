package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetExternalRequestDetailService;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateAssetRequestUsecase {

    private final AssetExternalRequestDetailService assetExternalRequestDetailService;
    private final AssetRequestService assetRequestService;


    public void execute(Long requestDetailId, HttpSession session) {

        Integer count = assetExternalRequestDetailService.countOptionDetail(requestDetailId);
        if(count > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status is invalid !");
        }

        AssetRequest assetRequest = assetRequestService.findByUpdate(requestDetailId);
        if (Objects.isNull(assetRequest)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request status is invalid !");
        }
        assetRequest.setRequestStatusId(RequestStatus.RESEARCH.getValue());
        assetRequestService.updatePurchaseRequestStatus(assetRequest);
    }
}

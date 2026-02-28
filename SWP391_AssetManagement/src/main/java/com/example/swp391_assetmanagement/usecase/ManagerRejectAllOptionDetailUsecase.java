package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetExternalRequestDetail;
import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.AssetType;
import com.example.swp391_assetmanagement.enums.ExternalStatus;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.AssetService;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerRejectAllOptionDetailUsecase {

    private final OptionDetailService optionDetailService;
    private final AssetRequestService assetRequestService;

    @Transactional
    public void execute(Long assetRequestId, HttpSession session) {

        List<OptionDetail> optionDetail = optionDetailService.getListByRequestDetailId(assetRequestId);

        if (!CollectionUtils.isEmpty(optionDetail)) {
            List<OptionDetail> optionDetails = optionDetail.stream()
                    .map(dto -> {
                        OptionDetail entity = new OptionDetail();
                        entity.setId(dto.id);
                        entity.setIsSelected(Boolean.FALSE);
                        return entity;
                    }).toList();
            optionDetailService.updateRejectAll(optionDetails);
        }

        AssetRequest assetRequest = assetRequestService.findByUpdate(assetRequestId);

        if (!ObjectUtils.isEmpty(assetRequest)) {
            assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue());
            assetRequestService.updateIsSelected(assetRequest);
        }

    }
}

package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerRejectAllOptionDetailUsecase {

    private final OptionDetailService optionDetailService;
    private final AssetRequestService assetRequestService;
    private final UserService userService;

    @Transactional
    public void execute(Long assetRequestDetailId, HttpSession session) {

        Long userId = userService.getIdByUserCode(session.getAttribute("USER_CODE").toString());

        List<OptionDetail> optionDetail = optionDetailService.getListByRequestDetailId(assetRequestDetailId);

        if (!CollectionUtils.isEmpty(optionDetail)) {
            List<OptionDetail> optionDetails = optionDetail.stream()
                    .map(dto -> {
                        OptionDetail entity = new OptionDetail();
                        entity.setId(dto.id);
                        entity.setApproverBy(userId);
                        entity.setApprovedDate(LocalDate.now());
                        entity.setIsSelected(Boolean.FALSE);
                        return entity;
                    }).toList();
            optionDetailService.updateRejectAll(optionDetails);

            Long assetRequestId = assetRequestService.findIdByAssetRequestDetailId(assetRequestDetailId);
            Integer countBySelected = optionDetailService.countByIdAndIsSelected(assetRequestDetailId, assetRequestId);

            AssetRequest assetRequest = assetRequestService.findByUpdate(assetRequestId);

            if (countBySelected == 0 && !ObjectUtils.isEmpty(assetRequest)){
                assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue());
                assetRequestService.updateIsSelected(assetRequest);
            }
        }

    }
}

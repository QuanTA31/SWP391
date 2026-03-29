package com.example.swp391_assetmanagement.usecase;

import com.example.swp391_assetmanagement.entity.AssetRequest;
import com.example.swp391_assetmanagement.entity.OptionDetail;
import com.example.swp391_assetmanagement.enums.RequestStatus;
import com.example.swp391_assetmanagement.enums.RequestType;
import com.example.swp391_assetmanagement.service.AssetRequestService;
import com.example.swp391_assetmanagement.service.OptionDetailService;
import com.example.swp391_assetmanagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManagerRejectAllLiquidationUsecase {

    private final OptionDetailService optionDetailService;
    private final AssetRequestService assetRequestService;
    private final UserService userService;

    @Transactional
    public void execute(Long assetRequestDetailId, HttpSession session) {

        Long assetRequestId = assetRequestService.findIdByAssetRequestDetailId(assetRequestDetailId);
        // Check type request
        String assetRequestType = assetRequestService.findRequestTypeById(assetRequestId);
        // neu requestype khong phai liquidation (error)
        if (ObjectUtils.isEmpty(assetRequestType)
                || !Objects.equals(RequestType.of(assetRequestType).getValue(), RequestType.LIQUIDATION.getValue())) {
            throw new ValidationException("Invalid request type");
        }
        // lay userId tu session (nguoi approve/reject)
        Long userId = userService.getIdByUserCode(session.getAttribute("USER_CODE").toString());

        Integer count = optionDetailService.countByIdAndStatus(assetRequestDetailId, Boolean.TRUE);

        //da co option dc chon → khong cho reject all
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

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

            Integer countBySelected = optionDetailService.countByIdAndIsSelected(assetRequestDetailId, assetRequestId);
            AssetRequest assetRequest = assetRequestService.findByUpdate(assetRequestId);

            // khong co option nao duoc approve
            // request ton tai
            // dang o status RESEARCH
            if (countBySelected == 0 && !ObjectUtils.isEmpty(assetRequest) && Objects.equals(assetRequest.requestStatusId, RequestStatus.RESEARCH.getValue())){
                // request -> APPROVED
                assetRequest.setRequestStatusId(RequestStatus.APPROVED.getValue());
                assetRequestService.updateIsSelected(assetRequest);
            }
        }
    }
}

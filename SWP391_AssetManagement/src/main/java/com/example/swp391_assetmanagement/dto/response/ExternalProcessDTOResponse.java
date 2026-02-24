package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ExternalProcessDTOResponse {

//    private final String assetId;

    private final String assetRequestName;

//    private final String requestStatusName;

    private final String assetTypeName;

    private final String externalStatusName;

    private final Long quantity;

//    private final LocalDate handoverDate;

    private final String note;

//    private final String approvalStatusName;
//
//    private final Long optionDetailId;

    private final LocalDate createdAt;

}

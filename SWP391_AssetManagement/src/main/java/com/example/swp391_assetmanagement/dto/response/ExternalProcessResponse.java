package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ExternalProcessResponse {

    private final String assetId;

    private final String assetTypeName;

    private final String requestStatusName;

    private final String requestTypeName;

    private final Long quantity;

    private final LocalDate handoverDate;

    private final String note;

    private final String approvalStatusName;

    private final Long optionDetailId;

}

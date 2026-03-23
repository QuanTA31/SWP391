package com.example.swp391_assetmanagement.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class CreateLiquidationDTOResponse {
    private Long assetRequestId;

    private String requestStatus;

    private LocalDate requestedDate;

    private String requestTypeId;

    private Boolean isSubmitted;

    private List<CreateLiquidationDetailDTOResponse> details;
}
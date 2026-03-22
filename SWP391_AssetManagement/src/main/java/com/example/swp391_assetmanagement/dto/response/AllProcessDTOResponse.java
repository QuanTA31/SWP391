package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AllProcessDTOResponse {

    private final Long id;

    private final String requestTypeName;

    private final String assetTypeName;

    private final boolean isInternal;

    private final String requestedBy;

    private final LocalDate requestedDate;

    private final String requestStatusName;

    private final String requestStatusId;

    private final String approvalBy;

    private final LocalDate approvalDate;

    private final LocalDate handoverDate;

    private final String note;

    private final LocalDate createdAt;
    
    private final Boolean isDone;

    private final Integer quantity;

    private final Integer assignedQuantity;
}

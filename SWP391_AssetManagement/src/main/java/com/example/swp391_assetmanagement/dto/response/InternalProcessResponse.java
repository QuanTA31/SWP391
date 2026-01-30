package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InternalProcessResponse {

    private final String assetId;

    private final String requestStatusName;

    private final String requestTypeName;

    private final Long fromUserId;

    private final Long toUserId;

    private final LocalDate dateOfExecution;

    private final LocalDate handoverDate;

    private final String note;

    private final String approvalStatusName;

}

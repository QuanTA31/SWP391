package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class AllProcessResponse {

    private final String requestTypeName;

    private final String requestedBy;

    private final LocalDate requestedDate;

    private final String requestStatusName;

    private final String approvalBy;

    private final LocalDate approvalDate;

    private final LocalDate handoverDate;

    private final String note;

    private final LocalDate createdAt;

}

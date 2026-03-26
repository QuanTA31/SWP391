package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AssetLifecycleRequestDTOResponse {

    private final Long requestId;

    private final String requestTypeName;

    private final String requestStatusName;

    private final LocalDate requestedDate;

    private final Long requestedBy;

    private final LocalDate approvedDate;

    private final Long approvedBy;

    private final LocalDate handoverDate;

    private final String note;
}

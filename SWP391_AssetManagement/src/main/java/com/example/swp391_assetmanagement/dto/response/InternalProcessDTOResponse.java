package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InternalProcessDTOResponse {

    private final Long assetId;

    private final String assetRequestName;

    private final String assetTypeName;

    private final Integer quantity;

    private final String fromLocationName;

    private final String toLocationName;

    private final String fromUserName;

    private final String toUserName;

//    private final LocalDate dateOfExecution;

//    private final LocalDate handoverDate;

    private final String note;

    private final LocalDate createdAt;

 //   private final String approvalStatusName;

}

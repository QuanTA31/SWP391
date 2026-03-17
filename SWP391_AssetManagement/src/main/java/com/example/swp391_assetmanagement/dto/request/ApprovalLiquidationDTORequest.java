package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
public class ApprovalLiquidationDTORequest {

    private Boolean isApproved;

    private Long assetRequestId;

    private String note;
}

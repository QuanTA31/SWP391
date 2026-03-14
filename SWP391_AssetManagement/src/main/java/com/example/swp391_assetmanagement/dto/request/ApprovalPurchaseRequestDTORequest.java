package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Getter
@Builder
public class ApprovalPurchaseRequestDTORequest {

    private Boolean isApproved;

    private Long assetRequestId;

    private String note;
}

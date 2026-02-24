package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApprovalPurchaseRequestDTORequest {

    private boolean isApproved;

    private Long assetRequestId;
}

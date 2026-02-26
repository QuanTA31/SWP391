package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApprovalPurchaseRequestDTORequest {

    private boolean isApproved;

    private Long assetRequestId;

    private String note;
}

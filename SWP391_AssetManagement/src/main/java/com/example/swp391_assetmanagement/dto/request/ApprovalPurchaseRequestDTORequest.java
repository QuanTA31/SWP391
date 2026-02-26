package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalPurchaseRequestDTORequest {

    private boolean isApproved;

    private Long assetRequestId;

    private String note;
}

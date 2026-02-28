package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePurchaseRequestDTORequest {

    private List<CreatePurchaseRequestDetailDTORequest>  createPurchaseRequestDetailDTORequestList;

    private boolean isSubmitted;

    private Long assetRequestId;

    private String requestStatus;

}

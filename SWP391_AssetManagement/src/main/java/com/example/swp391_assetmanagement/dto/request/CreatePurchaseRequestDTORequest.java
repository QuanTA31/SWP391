package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreatePurchaseRequestDTORequest {

    private List<CreatePurchaseRequestDetailDTORequest>  createPurchaseRequestDetailDTORequestList;

    private boolean isSubmitted;

    private Long assetRequestId;

}

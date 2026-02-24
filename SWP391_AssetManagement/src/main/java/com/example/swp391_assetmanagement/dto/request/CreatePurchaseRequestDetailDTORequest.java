package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePurchaseRequestDetailDTORequest {

    private String assetTypeId;

    private Integer quantity;

    private String note;
}

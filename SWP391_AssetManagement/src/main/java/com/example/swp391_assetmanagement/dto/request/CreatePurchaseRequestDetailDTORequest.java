package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Getter
@Builder
public class CreatePurchaseRequestDetailDTORequest {

    private Long assetExternalRequestDetailId;

    private String assetTypeId;

    private Integer quantity;

    private String note;

    private String assetTypeName;

    private String externalStatusId;
}

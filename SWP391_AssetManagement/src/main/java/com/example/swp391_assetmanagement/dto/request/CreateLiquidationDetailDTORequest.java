package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Setter
@Getter
@Builder
public class CreateLiquidationDetailDTORequest {

    private Long assetExternalRequestDetailId;

    private String assetTypeId;

    private Integer quantity;

    private String note;

    private String assetTypeName;

    private String externalStatusId;
}

package com.example.swp391_assetmanagement.dto.response;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateLiquidationDetailDTOResponse {
    private Long assetExternalRequestDetailId;
    private String assetTypeId;
    private String assetTypeName;
    private String externalStatusId;
    private Integer quantity;
    private String note;
}

package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateLiquidationDetailDTORequest {

    private Long assetExternalRequestDetailId;

    private String assetTypeId;

    private Integer quantity;

    private String note;

    private String assetTypeName;

    private String externalStatusId;
}

package com.example.swp391_assetmanagement.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePurchaseRequestDetailDTORequest {

    private String assetTypeId;

    private Integer quantity;

    private String note;
}

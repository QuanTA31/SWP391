package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemDTOResponse {

    private Long detailId;

    private String assetCode;

    private String userFullName;

    private String assetTypeName;

    private Boolean isDone;

    private String statusId;
}

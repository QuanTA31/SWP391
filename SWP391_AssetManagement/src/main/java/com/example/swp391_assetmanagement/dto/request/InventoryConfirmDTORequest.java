package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryConfirmDTORequest {
    private Long detailId;
    private Boolean isDone;
    private String note;
}

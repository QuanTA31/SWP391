package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InventoryProcessDTOResponse {
    private Long requestId;
    private String statusName;
    private List<InventoryItemDTOResponse> items;
}

package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryProcessDTORequest {

    private Long requestId;

    private String assetTypeId;

    private String fullName;
}

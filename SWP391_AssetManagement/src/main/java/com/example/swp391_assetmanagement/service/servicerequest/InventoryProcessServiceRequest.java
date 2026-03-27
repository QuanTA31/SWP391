package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryProcessServiceRequest {

    private Long requestId;

    private String assetTypeId;

    private String fullName;

    private long offset;

    private int pageSize;
}

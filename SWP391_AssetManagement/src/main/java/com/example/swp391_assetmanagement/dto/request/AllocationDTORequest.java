package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllocationDTORequest {

    // used for edit (null if creating a new draft)
    private Long assetRequestId;

    private String assetTypeId;

    private Long assetId;

    // used when allocationMode = "location"
    private String locationId;
    private Integer quantity;

    // reason / note for the request
    private String reason;

    // "draft" | "create"  (set by JS on button click)
    private String action;
    // List of assets assigned by manager (if status is IN_PROGRESS or later)
    private java.util.List<com.example.swp391_assetmanagement.entity.Assets> assignedAssets;
}

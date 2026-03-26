package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ViewAssetLifecycleDTOResponse {

    // Asset header info
    private final String assetCode;

    private final String assetTypeName;

    private final String assetStatusName;

    private final String locationName;

    private final LocalDate receivedDate;

    private final BigDecimal originalPrice;

    private final String description;

    // List of requests related to this asset
    private final List<AssetLifecycleRequestDTOResponse> requests;

    // Pagination & filter state
    private final AssetLifecycleFilterDTOResponse filters;
}

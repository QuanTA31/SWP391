package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class AssetResponse {

    private final String assetCode;

    private final String description;

    private final BigDecimal originalPrice;

    private final LocalDate warrantyPeriod;

    private final LocalDate receivedDate;

    private final String locationName;

    private final String assetStatusName;

    private final Long currentUserId;

    private final String assetTypeName;
}

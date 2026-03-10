package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class PurchaseAssetDTOResponse {

    private final String assetCode;

    private final String assetTypeName;

    public LocalDate warrantyPeriod;

    public BigDecimal originalPrice;

    public String description;

    public LocalDate receivedDate;
}

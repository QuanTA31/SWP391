package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class AssetViewAllServiceResponse {

    public String assetCode;

    public String description;

    public BigDecimal originalPrice;

    public LocalDate warrantyPeriod;

    public LocalDate receivedDate;

    public String locationId;

    public String assetStatusId;

    public Long currentUserId;

    public String assetTypeId;

    public Integer totalItems;
}

package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
public class PurchaseAssetAllServiceResponse {

    @Column(name = "asset_code")
    public String assetCode;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    @Column(name = "original_price")
    public BigDecimal originalPrice;

    @Column(name = "description")
    public String description;

    @Column(name = "received_date")
    public LocalDate receivedDate;

    @Column(name = "total_items")
    public Integer totalItems;
}

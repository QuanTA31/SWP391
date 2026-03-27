package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
public class PurchaseOrderItemResponse {

    @Column(name = "asset_external_request_detail_id")
    public Long assetExternalRequestDetailId;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "quantity")
    public Integer quantity;

    @Column(name = "note")
    public String note;

    @Column(name = "merchant")
    public String merchant;

    @Column(name = "description")
    public String description;

    @Column(name = "unit_price")
    public BigDecimal unitPrice;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    // Computed at usecase level – not mapped from DB
    public String assetTypeName;

    public BigDecimal totalPrice;
}

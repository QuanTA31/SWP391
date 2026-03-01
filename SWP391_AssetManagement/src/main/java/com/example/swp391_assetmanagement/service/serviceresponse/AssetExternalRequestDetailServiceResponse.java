package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
public class AssetExternalRequestDetailServiceResponse {

    @Column(name = "id")
    public Long id;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "quantity")
    public Integer quantity;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    @Column(name = "unit_price")
    public BigDecimal unitPrice;

    @Column(name = "description")
    public String description;

}

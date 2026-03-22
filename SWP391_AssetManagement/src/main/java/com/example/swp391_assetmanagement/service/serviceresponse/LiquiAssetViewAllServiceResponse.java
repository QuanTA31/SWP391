package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
public class LiquiAssetViewAllServiceResponse {

    @Column(name = "id")
    public String assetId;

    @Column(name = "asset_code")
    public String assetCode;

    @Column(name = "description")
    public String description;

    @Column(name = "original_price")
    public BigDecimal originalPrice;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    @Column(name = "received_date")
    public LocalDate receivedDate;

    @Column(name = "location_id")
    public String locationId;

    @Column(name = "asset_status_id")
    public String assetStatusId;

    @Column(name = "current_user_id")
    public Long currentUserId;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "total_items")
    public Integer totalItems;

    @Column(name = "is_selected")
    public Boolean isSelected;
}

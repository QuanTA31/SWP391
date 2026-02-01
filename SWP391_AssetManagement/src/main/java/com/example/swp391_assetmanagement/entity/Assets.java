package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "assets")
public class Assets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_code")
    public String assetCode;

    @Column(name = "asset_status_id")
    public String assetStatusId;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    @Column(name = "original_price")
    public BigDecimal originalPrice;

    @Column(name = "describe")
    public String describe;

    @Column(name = "current_user_id")
    public Long currentUserId;

    @Column(name = "location_id")
    public String locationId;

    @Column(name = "depreciation")
    public BigDecimal depreciation;

    @Column(name = "received_date")
    public LocalDateTime receivedDate;

    @Column(name = "note")
    public String note;

    @Column(name = "created_at")
    public LocalDateTime createdAt;
}

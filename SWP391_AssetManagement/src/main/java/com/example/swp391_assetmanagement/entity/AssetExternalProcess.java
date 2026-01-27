package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "asset_external_process")
public class AssetExternalProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_type_id")
    public String assetTypeId;

    @Column(name = "request_type_id")
    public String requestTypeId;

    @Column(name = "request_status_id")
    public String requestStatusId;

    @Column(name = "request_progress_management_id")
    public Long requestProgressManagementId;

    @Column(name = "quantity")
    public Long quantity;

    @Column(name = "assets_processed_quantity")
    public Long assetsProcessedQuantity;

    @Column(name = "expected_unit_price")
    public BigDecimal expectedUnitPrice;

    @Column(name = "actual_unit_price")
    public BigDecimal actualUnitPrice;

    @Column(name = "note")
    public String note;

    @Column(name = "reason")
    public String reason;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "handover_date")
    public LocalDate handoverDate;
}

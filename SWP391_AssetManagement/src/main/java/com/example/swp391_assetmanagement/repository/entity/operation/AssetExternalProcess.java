package com.example.swp391_assetmanagement.repository.entity.operation;

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

    @Column(name = "request_code")
    public String requestCode;

    @Column(name = "asset_type_id")
    public Long assetTypeId;

    @Column(name = "request_type_id")
    public Long requestTypeId;

    @Column(name = "request_status_id")
    public Long requestStatusId;

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

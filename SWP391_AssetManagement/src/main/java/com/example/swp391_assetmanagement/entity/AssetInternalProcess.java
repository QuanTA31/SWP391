package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "asset_internal_process")
public class AssetInternalProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_id")
    public Long assetId;

    @Column(name = "request_status_id")
    public Long requestStatusId;

    @Column(name = "request_type_id")
    public Long requestTypeId;

    @Column(name = "request_progress_management_id")
    public Long requestProgressManagementId;

    @Column(name = "from_user_id")
    public Long fromUserId;

    @Column(name = "to_user_id")
    public Long toUserId;

    @Column(name = "date_of_execution")
    public LocalDate dateOfExecution;

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

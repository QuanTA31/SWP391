package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Entity
@Table(name = "procurement_plan")
public class ProcurementPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_external_process_id")
    public Long assetExternalProcessId;

    @Column(name = "unit_price")
    public BigDecimal unitPrice;

    @Column(name = "description")
    public String description;

    @Column(name = "approval_status_id")
    public Long approvalStatusId;

    @Column(name = "merchant")
    public String merchant;

    @Column(name = "approver_by")
    public Long approverBy;

    @Column(name = "approved_at")
    public LocalDateTime approvedAt;
}


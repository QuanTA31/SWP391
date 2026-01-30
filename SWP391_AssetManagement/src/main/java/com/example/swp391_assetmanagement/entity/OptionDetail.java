package com.example.swp391_assetmanagement.entity;

import lombok.Setter;
import org.seasar.doma.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Entity
@Table(name = "option_detail")
public class OptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_request_detail_id")
    public Long assetRequestDetailId;

    @Column(name = "total_amount")
    public BigDecimal totalAmount;

    @Column(name = "describe")
    public String describe;

    @Column(name = "merchant")
    public String merchant;

    @Column(name = "is_selected")
    public Boolean isSelected;

    @Column(name = "approved_date")
    public Date approvedDate;

    @Column(name = "approver_by")
    public Long approverBy;

    @Column(name = "created_at")
    public LocalDateTime approvedAt;
}


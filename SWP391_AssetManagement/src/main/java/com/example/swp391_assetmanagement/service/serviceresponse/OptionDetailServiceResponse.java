package com.example.swp391_assetmanagement.service.serviceresponse;

import lombok.Getter;
import org.seasar.doma.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
public class OptionDetailServiceResponse {

    @Column(name = "id")
    public Long id;

    @Column(name = "asset_external_request_detail_id")
    public Long assetExternalRequestDetailId;

    @Column(name = "unit_price")
    public BigDecimal unitPrice;

    @Column(name = "description")
    public String description;

    @Column(name = "merchant")
    public String merchant;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    @Column(name = "is_selected")
    public Boolean isSelected;

    @Column(name = "approved_date")
    public LocalDate approvedDate;

    @Column(name = "approver_by")
    public Long approverBy;

    @Column(name = "created_at")
    public LocalDateTime createdAt;

    @Column(name = "approver_name")
    public String approverName;

}

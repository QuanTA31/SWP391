package com.example.swp391_assetmanagement.entity;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.seasar.doma.Transient;
import org.seasar.doma.Column;


@Getter
@Setter
@Entity
@Table(name = "option_detail")
public class OptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_external_request_detail_id")
    public Long assetExternalRequestDetailId;

    @Column(name = "unit_price")
    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 12, fraction = 2)
    public BigDecimal unitPrice;

    @Column(name = "describe")
    @NotBlank
    @Size(max = 255)
    public String description;

    @Column(name = "merchant")
    @NotBlank
    @Size(max = 120)
    public String merchant;

    @Column(name = "warranty_period")
    public LocalDate warrantyPeriod;

    @Column(name = "is_selected")
    public Boolean isSelected;

    @Column(name = "approved_date")
    public LocalDate approvedDate;

    @Column(name = "approver_by")
    public Long approverBy;

    @Column(name = "approver_name", insertable = false, updatable = false)
    public String approverName;


    @Column(name = "created_at", insertable = false, updatable = false)
    public LocalDateTime createdAt;
}


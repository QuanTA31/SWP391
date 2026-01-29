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
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "option_detail")
public class OptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "asset_external_process_id")
    public Long assetExternalProcessId;

    @Column(name = "unit_price")
    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 12, fraction = 2)
    public BigDecimal unitPrice;

    @Column(name = "description")
    @NotBlank
    @Size(max = 255)
    public String description;

    @Column(name = "approval_status_id")
    public String approvalStatusId;

    @Column(name = "merchant")
    @NotBlank
    @Size(max = 120)
    public String merchant;

    @Column(name = "approver_by")
    public Long approverBy;

    @Column(name = "approved_at")
    public LocalDateTime approvedAt;
}


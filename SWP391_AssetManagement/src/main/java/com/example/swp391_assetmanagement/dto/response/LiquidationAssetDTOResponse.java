package com.example.swp391_assetmanagement.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class LiquidationAssetDTOResponse {
    @Column(name = "assetId")
    public Long assetId;

    @Column(name = "assetCode")
    public String assetCode;

    @Column(name = "assetTypeName")
    public String assetTypeName;

    @Column(name = "merchant")
    public String merchant;

    @Column(name = "unitPrice")
    public java.math.BigDecimal unitPrice;

    @Column(name = "description")
    public String description;

    @Column(name = "approvedDate")
    public java.time.LocalDate approvedDate;
}

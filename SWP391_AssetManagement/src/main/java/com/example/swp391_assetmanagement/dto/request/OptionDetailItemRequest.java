package com.example.swp391_assetmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OptionDetailItemRequest {

    private BigDecimal unitPrice;
    private String description;
    private String merchant;
    private LocalDate warrantyPeriod;
}



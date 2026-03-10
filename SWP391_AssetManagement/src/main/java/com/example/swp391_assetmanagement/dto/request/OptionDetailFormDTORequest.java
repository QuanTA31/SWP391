package com.example.swp391_assetmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OptionDetailFormDTORequest {

    private Long id; // dùng cho edit

    private String merchant;

    private String description;

    private BigDecimal unitPrice;

    private LocalDate warrantyPeriod;
}


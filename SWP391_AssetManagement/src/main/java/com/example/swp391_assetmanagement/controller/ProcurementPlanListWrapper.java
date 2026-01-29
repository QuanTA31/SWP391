package com.example.swp391_assetmanagement.controller;

import com.example.swp391_assetmanagement.entity.OptionDetail;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class ProcurementPlanListWrapper {
    @Valid
    @NotEmpty
    private List<OptionDetail> plans;
}


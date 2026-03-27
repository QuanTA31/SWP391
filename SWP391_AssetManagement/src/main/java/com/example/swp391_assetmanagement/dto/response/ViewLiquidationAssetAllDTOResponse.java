package com.example.swp391_assetmanagement.dto.response;

import com.example.swp391_assetmanagement.dto.request.ViewLiquidationAssetDTORequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewLiquidationAssetAllDTOResponse {
    private List<LiquidationAssetDTOResponse> liquidationAssets;
    private ViewLiquidationAssetDTORequest filter;
    private java.math.BigDecimal totalAmount;
    private String requestStatus;
}

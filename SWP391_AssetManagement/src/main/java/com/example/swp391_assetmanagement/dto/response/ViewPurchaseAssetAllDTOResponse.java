package com.example.swp391_assetmanagement.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ViewPurchaseAssetAllDTOResponse {

    private final List<PurchaseAssetDTOResponse> purchaseAssetDTOResponses;

    private final FilterPurchaseAssetDTOResponse filterPurchaseAssetDTOResponse;
}

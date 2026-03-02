package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewPurchaseAssetDTORequest {

    private final Long assetRequestId;

    private String assetTypeId;

    private String searchWord;

    private Integer pageIndex;

}

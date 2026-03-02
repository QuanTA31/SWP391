package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseAssetAllServiceRequest {

    private String assetTypeId;

    private String searchWord;

    private Integer offset;

    private Integer pageSize;
}

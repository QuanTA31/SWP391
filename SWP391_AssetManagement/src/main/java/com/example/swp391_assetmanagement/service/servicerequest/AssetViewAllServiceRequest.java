package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetViewAllServiceRequest {

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private Integer pageIndex;

    private Integer pageSize;
}

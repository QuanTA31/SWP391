package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetViewAllRequest {

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private Integer offset;

    private Integer pageSize;
}

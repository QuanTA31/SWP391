package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LiquiAssetViewAllServiceRequest {

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private String searchWord;

    private Integer offset;

    private Integer pageSize;

    private Integer assetRequestId;
}

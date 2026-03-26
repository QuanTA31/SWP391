package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetLifecycleServiceRequest {

    private String assetCode;

    private String requestTypeId;

    private Integer offset;

    private Integer pageSize;
}

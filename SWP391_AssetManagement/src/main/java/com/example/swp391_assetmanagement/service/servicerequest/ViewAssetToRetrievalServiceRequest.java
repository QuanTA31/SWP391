package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAssetToRetrievalServiceRequest {

    private String assetCode;

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private int offset;

    private int pageSize;
}

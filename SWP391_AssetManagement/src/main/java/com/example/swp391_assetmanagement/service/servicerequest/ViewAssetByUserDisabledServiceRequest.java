package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAssetByUserDisabledServiceRequest {

    private String userStatus;

    private String name;

    private String locationId;

    private String assetTypeId;

    private int offset;

    private int pageSize;
}

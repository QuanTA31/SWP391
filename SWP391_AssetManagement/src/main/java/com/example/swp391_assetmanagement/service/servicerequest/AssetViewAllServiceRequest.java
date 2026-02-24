package com.example.swp391_assetmanagement.service.servicerequest;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AssetViewAllServiceRequest {

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private String searchWord;

    private List<String> locationIdList;

    private Integer offset;

    private Integer pageSize;
}

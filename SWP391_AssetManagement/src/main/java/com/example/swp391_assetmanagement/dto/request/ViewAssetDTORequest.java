package com.example.swp391_assetmanagement.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAssetDTORequest {

    private String locationId;

    private String assetTypeId;

    private String assetStatusId;

    private Integer pageIndex;
}
